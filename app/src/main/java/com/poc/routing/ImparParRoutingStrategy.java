package com.poc.routing;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Component("ImparPar")
public class ImparParRoutingStrategy implements RoutingStrategy {
    private final String[] evenHourShards = {"B", "D"};
    private final String[] oddHourShards = {"A", "C"};

    @Override
    public String[] getShardDb(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("DEBUG: Routing payload: " + payload);
            var node = mapper.readTree(payload);
            System.out.println("DEBUG: Parsed node: " + node);
            int id = node.has("id") ? node.get("id").asInt() : 0;
            System.out.println("DEBUG: Parsed ID: " + id);
            
            if (id % 2 == 0) {
                return evenHourShards;
            }
            return oddHourShards;
        } catch (IOException e) {
            return oddHourShards;
        }
    }
}
