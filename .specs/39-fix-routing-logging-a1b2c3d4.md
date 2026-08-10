# 39-fix-routing-logging-a1b2c3d4.md

## Goal
Diagnose and fix the routing failure in `ImparParRoutingStrategy` where shard selection is returning empty results.

## Definition of Done (DoD)
- [ ] Add diagnostic logging to `ImparParRoutingStrategy` to trace the JSON parsing of the payload.
- [ ] Ensure the payload is correctly parsed and the `id` field is retrieved.
- [ ] Verify if the routing logic correctly maps IDs to shards (A/C for odd, B/D for even).
- [ ] Run the full load test and confirm shard record counts are non-zero.
