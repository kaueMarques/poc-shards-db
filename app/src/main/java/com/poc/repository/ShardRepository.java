package com.poc.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShardRepository {

    private static final String INSERT_SQL = "INSERT INTO event_data (data) VALUES (:data)";
    private static final String SELECT_SQL = "SELECT data FROM event_data WHERE data = :data LIMIT 1";
    private static final String SELECT_ALL_SQL = "SELECT data FROM event_data";

    private final Map<String, NamedParameterJdbcTemplate> shardTemplates;

    @Autowired
    public ShardRepository(Map<String, NamedParameterJdbcTemplate> shardTemplates) {
        this.shardTemplates = shardTemplates;
    }

    public String process(String shardId, String data) {
        NamedParameterJdbcTemplate template = shardTemplates.get(shardId.toUpperCase());

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode node = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(data);
            node.put("shard_destination", shardId.toUpperCase());
            data = mapper.writeValueAsString(node);
        } catch (Exception e) {
            // Ignore and use original data if not JSON
        }

        MapSqlParameterSource params = new MapSqlParameterSource("data", data);

        template.update(INSERT_SQL, params);
        return template.queryForObject(SELECT_SQL, params, String.class);
    }
    public java.util.List<String> findAll(String shardId) {
        NamedParameterJdbcTemplate template = shardTemplates.get(shardId.toUpperCase());
        if (template == null) return java.util.Collections.emptyList();
        return template.queryForList(SELECT_ALL_SQL, new MapSqlParameterSource(), String.class);
    }

    public Map<String, String> processAll(String[] shards, String data) {
        Map<String, String> results = new HashMap<>();
        for (String shardId : shards) {
            String result = process(shardId, data);
            if (result != null) {
                results.put(shardId, result);
            }
        }
        return results;
    }
}
