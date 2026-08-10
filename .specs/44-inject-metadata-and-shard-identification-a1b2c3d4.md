# 44-inject-metadata-and-shard-identification-a1b2c3d4.md

## Goal
Inject `persisted_at` timestamp and `shard_destination` metadata into the JSON payload right before persisting. Expose the origin Shard when exporting the consolidated E2E test CSV.

## Target
- `ShardRepository.java`: Read JSON and inject metadata if possible before `MapSqlParameterSource`.
- `ShardRestController.java`: Update `fetchFromShards` to include the `shardId` mapped alongside the record `String`.
- `gerencia_infra.py`: Update the output renderer and CSV writer to correctly extract `_shard` from the updated nested structure.

## Verification
- Option 15 outputs metadata dynamically generated at the database layer (verified via debug log).
- Option 16 yields a CSV that shows `Shard,ID,Data` columns correctly mapping to `A`, `B`, `C`, and `D`.