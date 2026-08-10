# Agents and Project Rules

This document defines the operating rules for the `shards-db` PoC project.

## Operational Rules
1. **No Execution Without Request:** NEVER execute any action, modification, or code generation without explicit user request or planned implementation steps.
2. **Clean Code:** Adhere to strict Clean Code principles.
3. **No Nested IFs:** NEVER use nested `if` statements. Flatten logic using guard clauses, polymorphism, or strategy patterns.
4. **No Comments:** NEVER include comments in the source code. The code itself MUST be self-documenting. If clarification is needed, improve the code structure or naming.
5. **PoC Purpose:** This project is a Proof of Concept (PoC) for a database sharding structure.
6. **Testing Mandatory:** Every execution or feature implementation MUST be accompanied by a unit/integration test.
7. **Execution Spec Mandatory:** Every execution action MUST have a spec file in `.specs/`: `NN-short-description-uniqueID.md`.
8. **Project Structure:**
    - `app/`: Java source code (Spring Boot 3.3.0, Spring Cloud AWS 3.1.0).
    - `infra/`: Infrastructure as Code (Terraform) only. NO Java code here.
9. **Infrastructure Stack:**
    - Floci for AWS services (SQS, RDS).
    - Docker to encapsulate application and infrastructure components.
    - PostgreSQL containers for each shard (A, B, C, D).
10. **Docker Interaction Script:** All infrastructure management MUST be done via `gerencia_infra.py` (class-based, located in `gerencia-ambiente-local/`).
11. **Java Version:** 21.
12. **No Virtual Environments:** NEVER use Python `venv` in this project.
13. **No JPA/Hibernate:** NEVER use JPA or Hibernate. Spring Data JDBC is permitted ONLY for native SQL queries.
14. **README/Specs Mandatory:** Every project update MUST include corresponding updates to `README.md` and the appropriate `.specs/` file.
15. **CHANGELOG Update Mandatory:** Every project update MUST include a corresponding update to `CHANGELOG.md` reflecting the changes.
16. **Build and Deploy Process:** Updates to the application container MUST be performed via full container rebuild/restart.
17. **Infrastructure Destruction:** Destruction of infrastructure (including volumes) MUST be handled by the `gerencia_infra.py` script.
18. **Task Tracking:** Upon completion of any planned task, mark it as done using the `todo` tool.
19. **Shard Structure:** ALL database infrastructure MUST use a shard-per-instance structure.
20. **Plan Before Execution:** Always create a plan before code implementation.

## Definition of Done (DoD) Standard
Every task is considered done only when:
- [ ] Specification file `NN-short-description-uniqueID.md` created in `.specs/`.
- [ ] Requirements fully implemented.
- [ ] Clean code principles applied (no nested IFs).
- [ ] No comments in code (self-documenting only).
- [ ] Corresponding tests written and passing.
- [ ] Code properly structured under `app/` (Java) or `infra/` (Terraform).
- [ ] Dockerized implementation verified.
- [ ] Documentation (`README.md`, `AGENTS.MD`, `CHANGELOG.md`) updated.
