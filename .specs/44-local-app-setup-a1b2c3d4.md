# 44-local-app-setup-a1b2c3d4.md

## Goal
Adjust the project setup so the Java application runs natively on the local machine rather than in Docker, leaving only the database shards in the `docker-compose.yml`.

## Definition of Done (DoD)
- [x] Remove the `app` container from `infra/docker-compose.yml`.
- [x] Update `gerencia_infra.py` to no longer deploy the `app` to Docker.
- [x] Remove the leftover `spring-cloud-aws-starter-sqs` dependency from `pom.xml`.
- [x] Update `application.yml` with fallback URLs pointing to localhost for the local environment.
- [x] Add an execution spec (`.specs/44-local-app-setup-a1b2c3d4.md`).
- [x] Update `README.md` and `CHANGELOG.md`.

## Status
- [DONE] Remove the `app` container.
- [DONE] Update script logic.
- [DONE] Clean SQS dependency.
- [DONE] Adjust connection profiles.
