# 37-destroy-before-startup-m9n0o1p2.md

## Goal
Implement infrastructure destruction (container removal and volume cleanup) as a prerequisite to the `startup` command to ensure a clean state before initialization.

## Status
- [DONE] Update `startup()` in `gerencia_infra.py` to call `destroy()` before `configure_pool()` and `up()`.

## Definition of Done (DoD)
- [ ] O comando 'Startup' inicia com `destroy()`.
- [ ] Volumes são removidos antes de subir novos containers.
- [ ] Documentação (`CHANGELOG.md`) atualizada.
