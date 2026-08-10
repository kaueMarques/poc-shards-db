# 29-dynamic-datasource-config-j9k0l1m2.md

## Goal
Implement a fully shard-agnostic `DataSourceConfig` by removing manual bean definitions, allowing Spring to dynamically configure any number of shards defined in `application.yml` via a `Map<String, DataSourceProperties>`.

## Status
- [DONE] Refactor `DataSourceConfig` to use dynamic map processing.
- [DONE] Remove hardcoded `DataSource` beans.

## Definition of Done (DoD)
- [ ] No manual bean definitions for individual shards.
- [ ] `DataSourceConfig` dynamically handles any number of shards from YAML.
- [ ] Code properly structured.
- [ ] Documentation (`CHANGELOG.md`) updated.
