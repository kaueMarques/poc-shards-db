# 19-refactor-to-spring-threading-f8g9h0i1.md

## Goal
Refactor the event processing architecture to use Spring Boot's native `@Async` capabilities instead of manual thread management.

## Status
- [DONE] Update `Application.java` with `@EnableAsync`.
- [DONE] Refactor `ShardWorker.java` to a Spring `@Service` with `@Async` methods.
- [DONE] Create Spring-managed `SqsConsumer` for event dispatching.
- [DONE] Remove obsolete `WorkerInitializer.java` and `SqsForwarder.java`.

## Definition of Done (DoD)
- [ ] Application uses Spring-managed threading (`@Async`).
- [ ] Manual thread management (`Runnable`/`Thread`) removed.
- [ ] Functionality verified: consume from `entrada-aws`, process in shard thread, forward to `saida-aws`.
- [ ] No comments in code.
- [ ] Documentation updated.
