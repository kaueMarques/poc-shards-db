package com.poc.routing;

public interface RoutingStrategy {
    String[] getShardDb(String payload);
}
