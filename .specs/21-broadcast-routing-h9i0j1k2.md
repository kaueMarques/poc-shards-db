# 21-broadcast-routing-h9i0j1k2.md

## Goal
Implement broadcast insertion logic where the REST API endpoint `/process` triggers data insertion into all configured PostgreSQL shards instead of a single shard via round-robin.

## Status
- [DONE] Update `ShardRouter` to expose shard list.
- [DONE] Refactor `ShardRestController` to iterate over all shards and broadcast insertion/selection.

## Definition of Done (DoD)
- [ ] API successfully inserts data into all shards A, B, C, and D.
- [ ] API response aggregated to show results for all shards.
- [ ] No manual shard selection required in the route.
- [ ] Code properly structured and clean (no comments).
- [ ] Documentation (`README.md`, `CHANGELOG.md`) updated.
