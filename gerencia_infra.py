import time
import os
import subprocess
import argparse
import urllib.request
import json
import os
import argparse

COMPOSE_FILE = "infra/docker-compose.yml"

class AWSInitializer:
    def initialize(self):
        print("Initializing Infrastructure (SQS queues)...")
        env = os.environ.copy()
        env['AWS_ACCESS_KEY_ID'] = 'test'
        env['AWS_SECRET_ACCESS_KEY'] = 'test'
        env['AWS_DEFAULT_REGION'] = 'us-east-1'
        for queue_name in ['entrada-aws', 'saida-aws']:
            subprocess.run(["aws", "sqs", "create-queue", "--queue-name", queue_name, "--endpoint-url", "http://localhost:4566"], env=env)
        print("Infrastructure initialization complete.")

class InfraVerifier:
    def verify(self):
        print("Verifying Infrastructure...")
        env = os.environ.copy()
        env['AWS_ACCESS_KEY_ID'] = 'test'
        env['AWS_SECRET_ACCESS_KEY'] = 'test'
        env['AWS_DEFAULT_REGION'] = 'us-east-1'
        result = subprocess.run(["aws", "sqs", "list-queues", "--endpoint-url", "http://localhost:4566"], env=env, capture_output=True, text=True)
        if "entrada-aws" in result.stdout and "saida-aws" in result.stdout:
            print("SQS Queues exist.")
        else:
            print("SQS Queues NOT found!")
        running_containers = subprocess.check_output("docker ps --format '{{.Names}}'", shell=True).decode('utf-8').split('\n')
        for shard in ['A', 'B', 'C', 'D']:
            expected_name = f"infra_shard-{shard}_1"
            if expected_name in running_containers:
                print(f"PostgreSQL SHARD 'shard-{shard}' is running.")
            else:
                print(f"PostgreSQL SHARD 'shard-{shard}' ({expected_name}) NOT found or NOT running!")
        print("Verification complete.")

class AppManager:
    def build(self):
        print("Building project...")
        # Logs show in terminal by default if capture_output is not used
        result = subprocess.run(["mvn", "clean", "package", "-DskipTests"])
        if result.returncode == 0:
            print("Build successful.")
            return True
        else:
            print(f"Build failed: {result.stderr}")
            return False

    def test_flow(self):
        # SQS test disabled as requested, but persistence check remains.
        # To show payload, let's use a sample.
        sample_payload = {"id": 1, "data": "test-data"}
        print(f"\n--- Running Persistence Check with Payload: {sample_payload} ---")
        print("\nChecking persistence in shards...")
        for shard in ['A', 'B', 'C', 'D']:
            print(f"Checking shard-{shard}:")
            subprocess.run(f"docker exec infra_shard-{shard}_1 psql -U user -d shard-{shard} -c 'SELECT * FROM event_data;'", shell=True)

        print("Deployment successful.")


class SchemaInitializer:
    def initialize(self):
        print("Initializing Shard Schema...")

        for shard in ['A', 'B', 'C', 'D']:
            print(f"Creating table in shard-{shard}")
            for _ in range(10):
                result = subprocess.run(f"docker exec infra_shard-{shard}_1 pg_isready -U user", shell=True)
                if result.returncode == 0:
                    subprocess.run(f"docker exec infra_shard-{shard}_1 psql -U user -d shard-{shard} -c 'CREATE TABLE IF NOT EXISTS event_data (id SERIAL PRIMARY KEY, data TEXT);'", shell=True)
                    break
                time.sleep(2)
        print("Schema initialization complete.")

class QueueInitializer:
    def initialize(self):
        print("Initializing SQS Queues...")
        env = os.environ.copy()
        env['AWS_ACCESS_KEY_ID'] = 'test'
        env['AWS_SECRET_ACCESS_KEY'] = 'test'
        env['AWS_DEFAULT_REGION'] = 'us-east-1'
        for queue in ['entrada-aws', 'saida-aws']:
            subprocess.run(f"aws sqs create-queue --queue-name {queue} --endpoint-url http://localhost:4566", env=env, shell=True)
        print("SQS Queue initialization complete.")


def calculate_pool_size(tasks="10", shards="4"):
    result = subprocess.run(["bash", "./calculadora_de_pool_de_conexao.sh", tasks, shards], capture_output=True, text=True)
    return result.stdout.strip()

def configure_pool_interactive():
    print("\n--- Wizard de Cálculo de Pool de Conexões ---")
    try:
        tasks = input("Informe o número de tarefas ECS [default 10]: ") or "10"
        shards = input("Informe o número de shards [default 4]: ") or "4"
    except EOFError:
        print("\nEntrada EOF detectada. Usando valores padrão.")
        tasks, shards = "10", "4"
    
    if not (tasks.isdigit() and shards.isdigit()):
        print("Erro: Por favor, informe apenas números inteiros.")
        return "1"

    result = subprocess.run(["bash", "./calculadora_de_pool_de_conexao.sh", tasks, shards], capture_output=True, text=True)
    pool_size = result.stdout.strip()
    print(f"\nConfiguração calculada: {pool_size}")
    return pool_size

def up(pool_size="5"):
    print("Starting infrastructure...")
    env = os.environ.copy()
    env['POOL_SIZE'] = pool_size
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} up -d --force-recreate", env=env, shell=True)

def down():
    print("Stopping infrastructure...")
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} down", shell=True)

def destroy():
    print("Destroying infrastructure...")
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} down -v --remove-orphans", shell=True)

def status():
    print("Infrastructure status:")
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} ps", shell=True)

def startup():
    print("\n[Step 1/2] Building application JAR...")
    if not AppManager().build():
        print("Build failed. Aborting startup.")
        return

    print("\n[Step 2/2] Starting infrastructure...")
    print("Cleaning up existing containers...")
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} down -v --remove-orphans", shell=True)
    subprocess.run(f"docker-compose -f {COMPOSE_FILE} up -d", shell=True)

    print("Initializing infrastructure...")
    SchemaInitializer().initialize()

    # Run automated test flow
    
    print("Startup complete.")
def interactive_menu():
    while True:
        print("\n--- Gerencia Infra Menu ---")
        print("1. Startup (Up + Init)")
        print("2. Up")
        print("3. Down")
        print("4. Destroy (Down -v)")
        print("5. Status")
        print("6. Verify")
        print("7. Build App")
        print("8. Deploy App")
        print("9. Send Event and Verify Processing")
        print("11. Configure Pool Settings (Wizard)")
        print("13. Check Shard Record Counts")
        print("14. Send Generic Payload to /process")
        print("12. Exit")
        print("15. Run Full Load Test (11 records)")
        print("16. Testar Rotas Consolidadas (/impar e /par)")
        choice = input("Select an option: ")
        
        if choice == '1': startup()
        elif choice == '2': up()
        elif choice == '3': down()
        elif choice == '4': destroy()
        elif choice == '5': status()
        elif choice == '6': verify()
        elif choice == '7': build_app()
        elif choice == '8': deploy_app()
        # Removed send event routing
        elif choice == '11': configure_pool_interactive()
        elif choice == '13': check_shard_counts()
        elif choice == '14': send_generic_payload()
        elif choice == '15': run_full_load_test()
        elif choice == '16': test_impar_par_routes()
        elif choice == '12':
            print("Exiting.")
            break
        else:
            print("Invalid choice, please try again.")

def check_shard_counts():
    print("\n--- Checking Shard Record Counts ---")
    for shard in ['A', 'B', 'C', 'D']:
        print(f"Shard-{shard}:")
        subprocess.run(f"docker exec infra_shard-{shard}_1 psql -U user -d shard-{shard} -c 'SELECT count(*) FROM event_data;'", shell=True)

def verify():
    InfraVerifier().verify()

def build_app():
    AppManager().build()

def deploy_app():
    print("App deployment is now run locally (e.g. java -jar target/shards-db-1.0-SNAPSHOT.jar)")



def format_api_response(json_str):
    import json
    try:
        data = json.loads(json_str)
        print("\n+" + "-"*75 + "+")
        print(f"| {'API RESPONSE':^73} |")
        print("+" + "-"*15 + "+" + "-"*57 + "+")
        status_val = data.get('status')
        shards_val = data.get('shards', data.get('shards_queried', []))
        print(f"| {'Status':<13} | {str(status_val if status_val else 'consolidated'):<55} |")
        print(f"| {'Shards':<13} | {', '.join(shards_val):<55} |")
        
        results = data.get('results', {})
        consolidated = data.get('consolidated_data')
        total_records = data.get('total_records')

        if total_records is not None:
            print(f"| {'Total Records':<13} | {str(total_records):<55} |")

        if consolidated:
            print("+" + "-"*15 + "+" + "-"*57 + "+")
            print(f"| {'Result (All)':<13} | {str(consolidated[:2]) + ('...' if len(consolidated)>2 else ''):<55} |")
            print("+" + "-"*15 + "+" + "-"*57 + "+")
        elif results:
            print("+" + "-"*15 + "+" + "-"*57 + "+")
            print(f"| {'Shard':<13} | {'Result':<55} |")
            print("+" + "-"*15 + "+" + "-"*57 + "+")
            for shard, result in results.items():
                print(f"| {shard:<13} | {str(result):<55} |")
        
        print("+" + "-"*75 + "+")
    except Exception:
        print(f"Response: {json_str}")

def run_full_load_test():
    import json
    print("\n--- Running Full Load Test (11 records, 1/sec) ---")
    for i in range(1, 12):
        payload = json.dumps({"id": i, "data": f"test-data-{i}"})
        print(f"Enviando: {payload}")
        req = urllib.request.Request("http://localhost:8080/process", data=payload.encode('utf-8'), headers={'Content-Type': 'application/json'})
        try:
            with urllib.request.urlopen(req) as response:
                format_api_response(response.read().decode('utf-8'))
        except Exception as e:
            print(f"Error: {e}")
        time.sleep(1)
    check_shard_counts()

def test_impar_par_routes():
    import urllib.request
    import json
    import csv

    all_records = []

    print("\n--- Testando recuperação paralela Ímpar (/impar) ---")
    try:
        req = urllib.request.Request("http://localhost:8080/impar")
        with urllib.request.urlopen(req) as response:
            resp_text = response.read().decode('utf-8')
            format_api_response(resp_text)
            data = json.loads(resp_text)
            if 'consolidated_data' in data:
                for item in data['consolidated_data']:
                    record = json.loads(item['record'])
                    record['_shard'] = item['shard']
                    all_records.append(record)
    except Exception as e:
        print(f"Error: {e}")

    print("\n--- Testando recuperação paralela Par (/par) ---")
    try:
        req = urllib.request.Request("http://localhost:8080/par")
        with urllib.request.urlopen(req) as response:
            resp_text = response.read().decode('utf-8')
            format_api_response(resp_text)
            data = json.loads(resp_text)
            if 'consolidated_data' in data:
                for item in data['consolidated_data']:
                    record = json.loads(item['record'])
                    record['_shard'] = item['shard']
                    all_records.append(record)
    except Exception as e:
        print(f"Error: {e}")

    if all_records:
        csv_filename = "relatorio_consolidado.csv"
        with open(csv_filename, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(['API_Route_Shard', 'DB_Persisted_Shard', 'Routing_Type', 'ID', 'Data', 'Persisted_At'])
            for record in sorted(all_records, key=lambda x: x.get('id', 0)):
                writer.writerow([
                    record.get('_shard', 'N/A'),
                    record.get('shard_destination', 'N/A'),
                    record.get('routing_type', 'N/A'),
                    record.get('id', 'N/A'),
                    record.get('data', 'N/A'),
                    record.get('persisted_at', 'N/A')
                ])
        print(f"\n[OK] CSV '{csv_filename}' gerado com sucesso contendo {len(all_records)} registros!")


def send_generic_event():
    filename = input("Enter JSON filename: ")
    EventManager().send_generic_event(filename)

def send_generic_payload():
    payload = '{"id": 1, "data": "test-payload-123"}'
    print(f"Sending default payload: {payload}")
    import urllib.request
    req = urllib.request.Request("http://localhost:8080/process", data=payload.encode('utf-8'), headers={'Content-Type': 'application/json'})
    try:
        with urllib.request.urlopen(req) as response:
            format_api_response(response.read().decode('utf-8'))
    except Exception as e:
        print(f"Error: {e}")

    check_shard_counts()

def main():
    parser = argparse.ArgumentParser(description="Gerencia Infra")
    parser.add_argument("command", nargs='?', choices=["up", "down", "destroy", "status", "verify", "startup", "build", "deploy", "send-event"], help="Command to run")
    args = parser.parse_args()
    
    if args.command == "up": up()
    elif args.command == "down": down()
    elif args.command == "destroy": destroy()
    elif args.command == "status": status()
    elif args.command == "verify": verify()
    elif args.command == "startup": startup()
    elif args.command == "build": build_app()
    elif args.command == "deploy": deploy_app()
    elif args.command == "send-event": send_generic_payload()
    else:
        interactive_menu()

if __name__ == "__main__":
    main()
