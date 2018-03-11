package edu.agh.wfiis.solid.srp;

import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.Message;
import edu.agh.wfiis.solid.srp.model.raml.Constraints;
import edu.agh.wfiis.solid.srp.model.raml.Header;

import java.util.HashMap;
import java.util.Map;

public class HttpRestRequest {

    protected Message requestMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(Message requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Message validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        processHeaders();
        return requestMessage;
    }

    private void processHeaders() throws InvalidHeaderException {
        for (String expectedKey : validationConstraints.getHeaders().keySet()) {
            Header expected = (Header) validationConstraints.getHeaders().get(expectedKey);
            Map<String, String> incomingHeaders = getIncomingHeaders(requestMessage);

            String actual = (String) incomingHeaders.get(expectedKey);
            if ((actual == null) && (expected.isRequired())) {
                throw new InvalidHeaderException("Required header " + expectedKey + " not specified");
            }
            if ((actual == null) && (expected.getDefaultValue() != null)) {
                setHeader(expectedKey, expected.getDefaultValue());
            }
            if (actual != null) {
                if (!expected.validate(actual)) {
                    String msg = String.format("Invalid value '%s' for header %s.", new Object[]{actual, expected});
                    throw new InvalidHeaderException(msg);
                }
            }
        }
    }

    private void setHeader(String key, String value) {
        if (requestMessage.getInboundProperty("http.headers") != null) {
            ((Map) requestMessage.getInboundProperty("http.headers")).put(key, value);
        }
    }

    private Map<String, String> getIncomingHeaders(Message message) {
        Map<String, String> incomingHeaders = new HashMap<>();
        if (message.getInboundProperty("http.headers") != null) {
            incomingHeaders = message.<Map>getInboundProperty("http.headers");
        }
        return incomingHeaders;
    }
}
