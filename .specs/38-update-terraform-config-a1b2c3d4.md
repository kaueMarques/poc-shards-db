# 38-update-terraform-config-a1b2c3d4.md

## Goal
Update Terraform infrastructure definitions to align with the new 4-shard PostgreSQL structure (A-D) and SQS queue names (`entrada-aws`, `saida-aws`).

## Status
- [DONE] Update `main.tf` to define shards A-D and new SQS queues.

## Definition of Done (DoD)
- [ ] `main.tf` uses `entrada-aws` and `saida-aws` queues.
- [ ] `main.tf` defines shards A, B, C, and D.
- [ ] Engine updated to PostgreSQL.
- [ ] Documentation (`CHANGELOG.md`) updated.
