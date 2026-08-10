# 42-implement-thread-affinity-a1b2c3d4.md

## Goal
Implement true Thread Affinity where a specific Thread exclusively handles a specific Shard, executing operations in parallel and avoiding cross-shard locks.

## Target
- `ShardWorker.java`: Refactor to use a `Map<String, ExecutorService>` instead of standard `@Async`.
- `ShardRestController.java`: Call `ShardWorker.processEvent` to leverage the custom ExecutorServices and `CompletableFuture.allOf()` to await parallel execution.

## Verification
- Code must compile.
- `gerencia_infra.py` (Option 15) full load test must succeed and correctly report all records accurately inserted across the 4 shards in parallel.