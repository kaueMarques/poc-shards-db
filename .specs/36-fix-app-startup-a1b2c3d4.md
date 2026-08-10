# 36-fix-app-startup-a1b2c3d4.md

## Goal
Fix the application startup failure caused by AWS SQS region loading issues and prevent `docker-compose` stale state corruption.

## Definition of Done (DoD)
- [ ] Explicitly configure the AWS region in `SqsConfig.java`.
- [ ] Improve `gerencia_infra.py` startup logic to ensure a clean container environment.
- [ ] Verify application starts successfully and persists data.
