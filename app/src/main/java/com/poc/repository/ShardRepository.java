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

    private final Map<String, NamedParameterJdbcTemplate> shardTemplates;

    @Autowired
    public ShardRepository(Map<String, NamedParameterJdbcTemplate> shardTemplates) {
        this.shardTemplates = shardTemplates;
    }

    public String process(String shardId, String data) {
        NamedParameterJdbcTemplate template = shardTemplates.get(shardId.toUpperCase());
        if (template == null) return null;

        MapSqlParameterSource params = new MapSqlParameterSource("data", data);
        template.update(INSERT_SQL, params);
        return template.queryForObject(SELECT_SQL, params, String.class);
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
