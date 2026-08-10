package com.poc.controller;

import com.poc.service.ShardWorker;
import com.poc.service.ShardRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShardRestController {

    @Autowired
    private ShardWorker shardWorker;

    @Autowired
    private ShardRouter shardRouter;

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody String data) {
        String[] shards = shardRouter.getStrategy("ImparPar").getShardDb(data);
        Map<String, String> results = new HashMap<>();
        List<java.util.concurrent.CompletableFuture<Void>> futures = new ArrayList<>();

        for (String shardId : shards) {
            futures.add(shardWorker.processEvent(shardId, data).thenAccept(res -> {
                if (res != null) {
                    synchronized (results) {
                        results.put(shardId, res);
                    }
                }
            }));
        }
        
        java.util.concurrent.CompletableFuture.allOf(futures.toArray(new java.util.concurrent.CompletableFuture[0])).join();
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("status", "processed");
        response.put("shards", new ArrayList<>(results.keySet()));
        response.put("results", results);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
