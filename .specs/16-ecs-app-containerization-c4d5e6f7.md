# 16-ecs-app-containerization-c4d5e6f7.md

## Goal
Containerize the Java application to simulate an ECS task deployment, ensuring it depends on the infrastructure shards and Floci, and update the management script and documentation.

## Status
- [DONE] Create `Dockerfile` for Java application.
- [DONE] Update `infra/docker-compose.yml` with `app` service.
- [DONE] Update `AGENTS.MD` with ECS-like container rule.

## Definition of Done (DoD)
- [ ] `Dockerfile` created for Java app.
- [ ] `infra/docker-compose.yml` updated with `app` service dependency.
- [ ] `app` container simulated as ECS task.
- [ ] `AGENTS.MD` updated.
- [ ] Build and deployment verified (via `manage_infra.py` or similar).
