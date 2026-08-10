package com.poc.routing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("default")
public class DefaultRoutingStrategy implements RoutingStrategy {

    @Value("${app.shards}")
    private String[] allShards;

    @Override
    public String[] getShardDb(String payload) {
        return allShards;
    }
}
