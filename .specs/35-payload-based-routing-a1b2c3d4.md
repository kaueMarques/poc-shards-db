# 35-payload-based-routing-a1b2c3d4.md

## Goal
Implement routing logic where the shard selection (Odd/Even group) is determined by the `id` field in the message payload. If `id` is even, route to shards B and D. If `id` is odd, route to shards A and C.

## Definition of Done (DoD)
- [x] Update `ImparParRoutingStrategy` to inspect the payload `id`.
- [x] Implement `id % 2 == 0` for B/D shards (Even).
- [x] Implement `id % 2 != 0` for A/C shards (Odd).
- [x] Verify routing logic with a test message.
- [x] Update documentation.
