# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2026-08-09
- Removed `destroy()` from `startup()` to prevent unnecessary volume deletion.
- Added `down()` to `startup()` and used `--force-recreate` in `up()` for clean environment restarts without destroying volumes.
- Added granular feedback logs to `startup()` and `interactive_menu()` for better user experience.


### Added
- Initial project structure for database sharding PoC.
- `gerencia_infra.py`: Interactive script for infrastructure lifecycle management.
- `gerencia-ambiente-local/`: Dedicated directory for infrastructure helpers.
- `infra/docker-compose.yml`: Defined Floci service and four PostgreSQL shard containers (A, B, C, D).
- Java application containerization (`Dockerfile`).
- Spring Boot 3.3.0 integration for REST API and SQS messaging.
- `entrada-aws` and `saida-aws` SQS queues.
- `ImparPar` routing strategy.

### Changed
- Migrated Java version from 25 to 21 for Spring Boot compatibility.
- Replaced LocalStack with Floci.
- Refactored project to use Spring Boot 3.3.0, Spring Cloud AWS 3.1.0, and `NamedParameterJdbcTemplate`.
- Refactored `ShardRestController` to delegate all data operations to `ShardRepository`.
- Refactored `ShardWorker` to use `@Async` and Spring-managed threading.
- Refactored REST API to `/process` endpoint with automatic shard routing and JSON response.
- Renamed `ShardRouter.getShards()` to `ShardRouter.getDefault()` for clarity.
- Updated Terraform configuration in `infra/terraform/main.tf` to match shard naming (A-D) and queue names (`entrada-aws`, `saida-aws`).
- Refactored `DataSourceConfig` to dynamically configure DataSources based on `application.yml`.
- Moved `calculate_pool.sh` to the project root and updated `gerencia_infra.py`.
- Renamed `usePares` routing strategy to `ImparPar` and updated logic to split shards between A/C (odd) and B/D (even).

### Removed
- Removed JPA/Hibernate (prohibited).
- Removed redundant `SqsConsumer` and `SqsForwarder` logic.
- Removed obsolete `infra/init-scripts/` shell scripts.
- Removed `venv` (prohibited).
- Removed `ShardConstants.java`.

### Rules Enforced
- "No Comments" rule applied across the codebase.
- "README Update Mandatory" rule established.
- "CHANGELOG Update Mandatory" rule established.
