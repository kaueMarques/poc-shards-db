package com.poc.service;

import com.poc.repository.ShardRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ShardWorker {
    private final ShardRepository repository;

    public ShardWorker(ShardRepository repository) {
        this.repository = repository;
    }

    @Async
    public void processEvent(String shardId, String data) {
        repository.process(shardId, data);
    }
}
