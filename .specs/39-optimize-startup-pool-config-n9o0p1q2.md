# 39-optimize-startup-pool-config-n9o0p1q2.md

## Goal
Optimize the `startup` command by using default pool settings instead of forcing the interactive wizard, while maintaining the wizard as an option for manual configuration.

## Status
- [DONE] Update `startup()` to use default values.
- [DONE] Maintain `configure_pool()` for manual override.

## Definition of Done (DoD)
- [ ] `startup` runs without interactive prompts.
- [ ] Interactive wizard remains available as a separate option.
- [ ] Code properly structured.
- [ ] Documentation (`CHANGELOG.md`) updated.
