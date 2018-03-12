package edu.agh.wfiis.solid.srp.model.raml;

import java.util.Map;

public class Constraints {

    private Map<String, Header> headers;

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }
}
