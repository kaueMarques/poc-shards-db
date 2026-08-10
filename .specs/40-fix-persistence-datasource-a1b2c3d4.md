# 40-fix-persistence-datasource-a1b2c3d4.md

## Goal
Fix the persistence issue by diagnosing and fixing the `DataSourceConfig` template map initialization, ensuring `ShardRepository` correctly receives all shard templates.

## Definition of Done (DoD)
- [ ] Add logging to `ShardRepository` constructor to trace the available shard templates.
- [ ] Ensure `DataSourceConfig` is correctly populating the `shardTemplates` map from `application.yml`.
- [ ] Verify with the test flow that records are correctly appearing in shard databases.
