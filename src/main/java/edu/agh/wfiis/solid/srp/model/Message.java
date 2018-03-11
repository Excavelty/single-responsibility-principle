package edu.agh.wfiis.solid.srp.model;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private Map<String, Object> inboundProperties;

    public Map<String, String> getInboundProperty(String property) {
        return (Map<String, String>) inboundProperties.get(property);
    }

    public void setInboundProperty(String property, Object value) {
        if(inboundProperties == null){
            inboundProperties = new HashMap<>();
        }
        inboundProperties.put(property, value);
    }
}
