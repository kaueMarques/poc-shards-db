# 33-imparpar-strategy-l5m6n7o8.md

## Goal
Rename the routing strategy to `ImparPar`, ensuring traffic is split between shards B/D during even hours and A/C during odd hours.

## Status
- [DONE] Rename `EvenHourRoutingStrategy` to `ImparParRoutingStrategy`.
- [DONE] Update strategy logic to return A/C during odd hours.
- [DONE] Update `ShardRestController` to use "ImparPar" strategy.

## Definition of Done (DoD)
- [ ] Strategy logic properly implemented and split between B/D (even) and A/C (odd).
- [ ] `ShardRestController` calls `ImparPar` strategy.
- [ ] Code is self-documenting.
- [ ] Documentation (`CHANGELOG.md`) updated.
