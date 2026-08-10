#!/bin/bash

read -p "Informe o número de tarefas ECS [default 10]: " TASKS
TASKS=${TASKS:-10}

read -p "Informe o número de shards [default 4]: " SHARDS
SHARDS=${SHARDS:-4}

POOL_SIZE=$(( TASKS * SHARDS ))

if [ "$POOL_SIZE" -lt 1 ]; then
    POOL_SIZE=1
fi

BUFFER=5
MIN_DB_CONNECTIONS=$(( POOL_SIZE + BUFFER ))

echo "------------------------------------------" >&2
echo "Memória de Cálculo:" >&2
echo "Tarefas: $TASKS" >&2
echo "Shards: $SHARDS" >&2
echo "Fórmula: (Tarefas * Shards) = Pool Size" >&2
echo "Cálculo: ($TASKS * $SHARDS) = $POOL_SIZE" >&2
echo "Recomendação para Postgres 'max_connections': >= $MIN_DB_CONNECTIONS" >&2
echo "------------------------------------------" >&2

echo "$POOL_SIZE"
