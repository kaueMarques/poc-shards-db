package com.poc.controller;

import com.poc.repository.ShardRepository;
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
    private ShardRepository shardRepository;

    @Autowired
    private ShardRouter shardRouter;

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody String data) {
        String[] shards = shardRouter.getStrategy("ImparPar").getShardDb(data);
        Map<String, String> results = shardRepository.processAll(shards, data);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("status", "processed");
        response.put("shards", new ArrayList<>(results.keySet()));
        response.put("results", results);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
