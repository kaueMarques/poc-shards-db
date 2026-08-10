# 10-update-db-instance-size-a1b2c3d6.md

## Objective
Update the Aurora shard instance class from `db.t3.medium` to `db.t3.micro` to use smaller database instances.

## DoD
- [DONE] Update `infra/terraform/main.tf` to set `instance_class` to `db.t3.micro`.
- [DONE] Status section updated to [DONE].
- [DONE] Tasks marked as done in todo.

## Status
- [DONE] Update instance class in `infra/terraform/main.tf`
