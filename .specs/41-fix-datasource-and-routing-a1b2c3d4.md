# 41-fix-datasource-and-routing-a1b2c3d4.md

## Goal
Resolve the persistence issue by correctly configuring Spring Boot's DataSource to avoid conflicts with auto-configuration and restore the `send_generic_payload` function.
## Definition of Done (DoD)
- [x] Refactor `application.yml` to move shard configuration out of the standard `spring.datasource` block to prevent conflicts.
- [x] Update `DataSourceConfig.java` to match the new configuration structure (`@ConfigurationProperties("database")`).
- [x] Restore `send_generic_payload` function in `gerencia_infra.py`.
- [x] Verify successful data persistence (non-zero record counts) using the end-to-end test.
