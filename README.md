# PoC Shards DB

Proof of Concept (PoC) desenvolvida para demonstrar uma arquitetura de banco de dados distribuído (sharding) utilizando Java 25, Spring Boot 3.3.0 e instâncias PostgreSQL isoladas. A aplicação distribui requisições recebidas via API REST para diferentes bancos de dados com base no conteúdo (payload).

## 1. Arquitetura Geral

A arquitetura da PoC é composta por:
- **Aplicação:** Uma API Spring Boot rodando em um container Docker, responsável por receber requisições REST (`/process`), determinar o destino dos dados (routing) e processar a persistência de forma assíncrona.
- **Camada de Persistência:** 4 bancos de dados PostgreSQL (Shards A, B, C e D) operando em containers completamente isolados.
- **Gerenciamento de Infraestrutura:** Toda a orquestração do ambiente (build, deploy, inicialização do schema, testes e teardown) é controlada através do script Python interativo `gerencia_infra.py`, que atua como interface principal sobre o Docker Compose e o Maven.

## 2. Roteamento (Sharding)

O processo de decisão de qual banco de dados receberá os dados é feito no momento em que a API recebe o payload JSON na rota `/process`. A lógica principal é regida pela classe `ImparParRoutingStrategy`.

A aplicação extrai o campo `id` do payload e aplica uma regra de paridade simples:
- **ID Ímpar (`id % 2 != 0`):** O dado é roteado e salvo nos shards **A** e **C**.
- **ID Par (`id % 2 == 0`):** O dado é roteado e salvo nos shards **B** e **D**.

Isso garante uma distribuição de carga previsível baseada nos dados do cliente.

## 3. Persistência (Bancos Isolados)

Para que a aplicação consiga persistir de maneira dinâmica em múltiplos bancos de dados, sem o uso de ORMs pesados (como JPA/Hibernate), a classe `DataSourceConfig` gerencia a criação isolada das conexões:

- A configuração do Spring desabilita propositalmente a auto-configuração de `DataSource` (via `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})`).
- O `application.yml` define blocos de configuração sob o prefixo `database.shards` (A, B, C, D).
- O `DataSourceConfig` lê essas configurações, itera sobre elas, e instancia **conexões HikariCP isoladas** para cada banco, gerando um mapa de `NamedParameterJdbcTemplate`s específicos para cada shard. Isso mantém as pools e transações totalmente estanques.

### ⚠️ Cuidado com o Pool de Conexões

O dimensionamento correto do Pool de Conexões (HikariCP) é um pilar crucial desta arquitetura. O cálculo é guiado pelo script `calculadora_de_pool_de_conexao.sh` (executado dinamicamente durante a subida do ambiente).

A fórmula baseia-se na multiplicação de **Tarefas x Shards** (`Tasks * Shards`). 
- Cada aplicação Spring Boot (tarefa) abre um pool fixo (`maximum-pool-size`) contra *cada um dos shards*. 
- Como temos 4 bancos de dados (shards), se tivéssemos 10 tarefas executando concorrentemente, o PostgreSQL precisaria suportar pelo menos 40 conexões persistentes só de pool ativo para evitar enfileiramento e eventual "connection refused" ou indisponibilidade por esgotamento de conexões (`max_connections`).
- O script automatiza essa matemática para proteger o banco de dados contra estrangulamento de recursos, ao garantir que as configurações geradas evitem que a aplicação sobrecarregue os bancos de dados em cenários de escala.

## 4. Uso de Threads (Assincronicidade)

Para maximizar o *throughput* (vazão de processamento) e garantir que a aplicação possa escalar sob alta carga, adotamos um modelo multithread no processamento dos eventos.

- A aplicação principal possui a anotação `@EnableAsync`, ativando o suporte à execução de processos em background via pools de threads gerenciadas pelo próprio Spring Boot.
- Na camada de serviço, a classe responsável pela persistência real (como o `ShardWorker`) utiliza a anotação `@Async`.
- **Funcionamento da Thread:** Quando a requisição REST chega no servidor embarcado (Tomcat), o roteador decide os shards de destino e repassa o comando para o service. Como o método é assíncrono, a thread principal da requisição web (HTTP request thread) é liberada quase que imediatamente para atender novos clientes externos. Simultaneamente, o Spring Boot aloca *threads internas em background* para estabelecer a comunicação JDBC e executar os `INSERT`s nos bancos de dados paralelos sem bloquear a camada web.

## 5. Como Testar End-to-End (E2E)

Todo o teste de validação de ponta a ponta pode ser conduzido de maneira interativa:

1. **Inicie o script gestor:**
   ```bash
   python3 gerencia_infra.py
   ```
2. **Suba todo o ambiente e recompile:**
   Selecione a **Opção 1 (Startup)**. O script vai:
   - Limpar builds antigos e rodar a compilação local: `mvn clean package`.
   - Construir a imagem Docker da aplicação injetando o novo arquivo `.jar`.
   - Limpar qualquer infraestrutura Docker pendente (`down -v`) e subir todos os containers recém compilados via Docker Compose.
   - Forçar a criação da tabela SQL (`event_data`) via `SchemaInitializer` em todos os shards criados.
3. **Validação E2E Básica e Visualização:**
   Selecione a **Opção 14 (Send Generic Payload to /process)**. O script enviará um JSON formatado (`id: 1`) para a API. A aplicação retornará quais shards foram afetados e a resposta bruta será automaticamente convertida pelo script Python em uma **tabela clara e legível diretamente no terminal** para facilitar a análise:
   ```text
   +---------------------------------------------------------------------------+
   |                               API RESPONSE                                |
   +---------------+---------------------------------------------------------+
   | Status        | processed                                               |
   | Shards        | A, C                                                    |
   +---------------+---------------------------------------------------------+
   | Shard         | Result                                                  |
   +---------------+---------------------------------------------------------+
   | A             | {"id": 1, "data": "test-payload-123"}                   |
   | C             | {"id": 1, "data": "test-payload-123"}                   |
   +---------------------------------------------------------------------------+
   ```
   Em seguida, o script já realiza consultas diretas nos shards (`SELECT count(*)`) conferindo que o dado realmente foi persistido de forma permanente no disco.
4. **Teste de Carga Completo:**
   Selecione a **Opção 15 (Run Full Load Test)**. Essa suíte de testes enviará automaticamente uma rajada de 11 requisições sequenciais (variando IDs pares e ímpares) à aplicação, e também imprimirá de maneira tabelada a resposta de cada requisição. Ao final da carga de dados, o script rodará as verificações em banco de dados atestando o real balanceamento (shards Ímpar e Par preenchendo todos os bancos concorrentemente sob estresse com a contagem exata de linhas).

## Regras e Processos
- **Versão Java:** 25.
- **Não usar `venv`:** Ambientes virtuais Python são proibidos.
- **Não usar JPA/Hibernate:** Proibidos. Spring Data JDBC é permitido apenas para queries SQL nativas e chamadas JDBC baseadas em template.
- **Atualizações do README:** Obrigatórias a cada mudança substancial de arquitetura.
- **Specs:** Obrigatórias para cada ação de execução em `.specs/`.
