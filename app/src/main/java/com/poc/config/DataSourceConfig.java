package com.poc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@ConfigurationProperties(prefix = "database")
public class DataSourceConfig {

    private Map<String, DataSourceProperties> shards = new HashMap<>();

    public Map<String, DataSourceProperties> getShards() {
        return shards;
    }

    public void setShards(Map<String, DataSourceProperties> shards) {
        this.shards = shards;
    }

    @Bean
    public Map<String, NamedParameterJdbcTemplate> shardTemplates() {
        Map<String, NamedParameterJdbcTemplate> templates = new HashMap<>();
        shards.forEach((key, props) -> {
            DataSource ds = props.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            templates.put(key.toUpperCase(), new NamedParameterJdbcTemplate(ds));
        });
        return templates;
    }

    @Bean
    public Map<String, BlockingQueue<String>> shardQueues() {
        Map<String, BlockingQueue<String>> queues = new HashMap<>();
        shards.keySet().forEach(key -> queues.put(key.toUpperCase(), new LinkedBlockingQueue<>()));
        return queues;
    }
}
