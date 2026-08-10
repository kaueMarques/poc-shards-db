# 43-add-consolidated-routes-a1b2c3d4.md

## Goal
Implement `/impar` and `/par` routes to quickly sweep related databases in parallel and consolidate the results in a single unified view.

## Target
- `ShardRepository.java`: Add `findAll(String shardId)` using `queryForList`.
- `ShardWorker.java`: Add `findAll(String shardId)` executing asynchronously in the shard's dedicated Executor.
- `ShardRestController.java`: Add endpoints for `/impar` (shards A,C) and `/par` (shards B,D) gathering records using `CompletableFuture.allOf()` and merging into a single `List<String>`.
- `gerencia_infra.py`: Add Option 16 to automatically query both endpoints and render the tabulated `API RESPONSE` displaying `Total Records` and a preview of `Result (All)`.

## Verification
- Run Option 15 to populate.
- Run Option 16 to query.
- Output tabulated correctly.
- Commit and push.