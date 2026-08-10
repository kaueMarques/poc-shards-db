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

    @GetMapping("/impar")
    public ResponseEntity<Map<String, Object>> getImpar() {
        return fetchFromShards(new String[]{"A", "C"});
    }

    @GetMapping("/par")
    public ResponseEntity<Map<String, Object>> getPar() {
        return fetchFromShards(new String[]{"B", "D"});
    }

    private ResponseEntity<Map<String, Object>> fetchFromShards(String[] shards) {
        java.util.List<Map<String, Object>> consolidatedData = new java.util.concurrent.CopyOnWriteArrayList<>();
        List<java.util.concurrent.CompletableFuture<Void>> futures = new ArrayList<>();

        for (String shardId : shards) {
            futures.add(shardWorker.findAll(shardId).thenAccept(res -> {
                if (res != null) {
                    for (String record : res) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("shard", shardId);
                        item.put("record", record);
                        consolidatedData.add(item);
                    }
                }
            }));
        }
        
        java.util.concurrent.CompletableFuture.allOf(futures.toArray(new java.util.concurrent.CompletableFuture[0])).join();
        Map<String, Object> response = new HashMap<>();
        response.put("shards_queried", shards);
        response.put("total_records", consolidatedData.size());
        response.put("consolidated_data", consolidatedData);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody String data) {
        Map<String, String> results = new HashMap<>();
        List<java.util.concurrent.CompletableFuture<Void>> futures = new ArrayList<>();

        long numericTimestamp = java.time.Instant.now().getEpochSecond();
        String routingType = (numericTimestamp % 2 == 0) ? "PAR" : "IMPAR";
        String enrichedData = data;
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode node = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(data);
            node.put("persisted_at", numericTimestamp);
            node.put("routing_type", routingType);
            enrichedData = mapper.writeValueAsString(node);
        } catch (Exception e) {
            // Proceed with original data if parsing fails
        }

        final String finalData = enrichedData;
        String[] shards = shardRouter.getStrategy("ImparPar").getShardDb(finalData);

        for (String shardId : shards) {
            futures.add(shardWorker.processEvent(shardId, finalData).thenAccept(res -> {
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
