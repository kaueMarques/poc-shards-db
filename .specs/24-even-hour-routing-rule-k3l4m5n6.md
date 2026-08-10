# 24-even-hour-routing-rule-k3l4m5n6.md

## Goal
Implement a dynamic routing rule where requests are broadcast only to shards B and D if the current hour is even. Otherwise, broadcast to all shards (A, B, C, D).

## Status
- [DONE] Update `ShardRouter` to incorporate the even-hour routing logic.
- [DONE] Validate routing behavior.

## Definition of Done (DoD)
- [ ] Shards B and D used during even hours.
- [ ] All shards used during odd hours.
- [ ] Code is self-documenting (no comments).
- [ ] Documentation updated (`CHANGELOG.md`, `README.md`).
