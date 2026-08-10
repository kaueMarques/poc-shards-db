package com.poc.service;

import com.poc.repository.ShardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ShardWorker {
    private final ShardRepository repository;
    private final Map<String, ExecutorService> shardExecutors = new HashMap<>();

    @Autowired
    public ShardWorker(ShardRepository repository, Map<String, NamedParameterJdbcTemplate> shardTemplates) {
        this.repository = repository;
        shardTemplates.keySet().forEach(shardId -> {
            shardExecutors.put(shardId.toUpperCase(), Executors.newSingleThreadExecutor(
                r -> new Thread(r, "Thread-" + shardId.toUpperCase())
            ));
        });
    }

    public CompletableFuture<String> processEvent(String shardId, String data) {
        ExecutorService executor = shardExecutors.get(shardId.toUpperCase());
        if (executor == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        return CompletableFuture.supplyAsync(() -> repository.process(shardId, data), executor);
    }

    @PreDestroy
    public void shutdown() {
        shardExecutors.values().forEach(ExecutorService::shutdown);
    }
}
