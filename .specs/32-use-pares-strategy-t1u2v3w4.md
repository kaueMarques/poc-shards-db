# 32-use-pares-strategy-t1u2v3w4.md

## Goal
Switch the default routing strategy in `ShardRestController` to `usePares` to ensure traffic is routed to shards B and D during even hours, and all shards otherwise.

## Status
- [DONE] Update `ShardRestController` to use the `usePares` strategy.

## Definition of Done (DoD)
- [ ] `ShardRestController` now calls `shardRouter.getStrategy("usePares")`.
- [ ] Routing rule correctly implemented.
- [ ] Documentation (`CHANGELOG.md`) updated.
