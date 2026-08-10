# 09-terraform-infra-mirror-d4e5f6a7.md

## Objective
Create Terraform configuration in `infra/terraform` that mirrors the current LocalStack infrastructure (4 Aurora Shards + 1 SQS Queue).

## Plan
1. Create `infra/terraform/` directory.
2. Define `provider.tf` configured for LocalStack.
3. Define `main.tf` with resource definitions for 4 RDS/Aurora instances and 1 SQS Queue.

## Definition of Done (DoD)
- [ ] `infra/terraform` directory exists.
- [ ] `provider.tf` configured for LocalStack endpoint.
- [ ] `main.tf` defines 4 Aurora shards and 1 SQS queue resources.
- [ ] Status section updated to [DONE].
- [ ] Tasks marked as done in todo.

## Status
- [DONE] Create 09-terraform-infra-mirror-d4e5f6a7.md (Spec)
- [DONE] Create infra/terraform directory
- [DONE] Create provider.tf for LocalStack
- [DONE] Create main.tf for Aurora shards and SQS
