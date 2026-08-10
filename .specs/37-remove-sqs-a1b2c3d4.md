# 37-remove-sqs-a1b2c3d4.md

## Goal
Remove all SQS-related infrastructure, messaging configuration, and dependencies to simplify the PoC architecture to just the REST API route.

## Definition of Done (DoD)
- [x] Remove `floci` service from `infra/docker-compose.yml`.
- [x] Delete `SqsConsumer.java`, `SqsProducer.java`, and `SqsConfig.java`.
- [x] Remove SQS-related dependencies/imports from `ShardWorker.java` and `Application.java`.
- [x] Verify the project builds and the application starts without SQS dependencies.
