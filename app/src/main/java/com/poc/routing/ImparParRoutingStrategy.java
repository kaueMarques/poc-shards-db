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
            String type = node.has("routing_type") ? node.get("routing_type").asText() : "";
            System.out.println("DEBUG: Parsed Routing Type: " + type);
            
            if ("PAR".equals(type)) {
                return evenHourShards;
            }
            return oddHourShards;
        } catch (IOException e) {
            return oddHourShards;
        }
    }
}
