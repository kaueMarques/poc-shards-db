# 17-shard-processing-a1b2c3d4.md

## Goal
Implement event processing where incoming SQS events from `entrada-aws` are inserted into the corresponding PostgreSQL shard, selected back, and forwarded to `saida-aws`. Also, implement a REST POST route for individual shard processing.

## Status
- [ ] Create spec file (this file).
- [ ] Update `AWSInitializer` to create database schema.
- [ ] Update `AppServer.java` to handle multi-threaded processing.
- [ ] Update `ShardController.java` to implement POST `/shard/{id}/process`.
- [ ] Update `README.md`.

## Definition of Done (DoD)
- [ ] Event processed: INSERT, SELECT, forward to `saida-aws`.
- [ ] Individual thread per shard for processing.
- [ ] REST API POST `/shard/{id}/process` implemented.
- [ ] No JPA/Hibernate used.
- [ ] Documentation updated.
