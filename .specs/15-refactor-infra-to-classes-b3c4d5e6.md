# 15-refactor-infra-to-classes-b3c4d5e6.md

## Goal
Consolidate auxiliary infrastructure scripts into class-based modules within the `gerencia-ambiente-local/` directory and update `gerencia_infra.py` to utilize these classes for better maintainability and organization.

## Status
- [DONE] Create `gerencia-ambiente-local/` directory.
- [DONE] Implement `AWSInitializer` class.
- [DONE] Implement `InfraVerifier` class.
- [DONE] Update `gerencia_infra.py` to use classes.
- [DONE] Remove obsolete scripts (`infra/init-scripts/`).

## Definition of Done (DoD)
- [ ] Auxiliary infrastructure scripts implemented as Python classes in `gerencia-ambiente-local/`.
- [ ] `gerencia_infra.py` successfully invokes these classes.
- [ ] Obsolete `infra/init-scripts/` directory removed.
- [ ] Code properly structured.
- [ ] Verification of infrastructure lifecycle still passing.
