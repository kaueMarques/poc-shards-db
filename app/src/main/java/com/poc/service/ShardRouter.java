package com.poc.service;

import com.poc.routing.RoutingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ShardRouter {
    @Autowired
    private Map<String, RoutingStrategy> strategies;

    public RoutingStrategy getStrategy(String name) {
        return strategies.getOrDefault(name, strategies.get("default"));
    }
}
