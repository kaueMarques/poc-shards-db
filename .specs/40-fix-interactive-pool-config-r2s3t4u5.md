# 40-fix-interactive-wizard-eof-r2s3t4u5.md

## Goal
Fix the `EOFError` when running the interactive menu in non-interactive environments (or during automated tests) by ensuring the wizard handles input gracefully, while maintaining usability in real interactive sessions.

## Status
- [DONE] Identify the cause of EOFError in interactive wizard.
- [TODO] Implement a fallback or check for interactive input in `configure_pool_interactive`.

## Definition of Done (DoD)
- [ ] Script runs successfully without `EOFError` when input is missing.
- [ ] Interactive menu remains functional for manual use.
- [ ] Code properly structured.
- [ ] Documentation (`CHANGELOG.md`) updated.
