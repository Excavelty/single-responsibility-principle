package edu.agh.solid.srp;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.module.apikit.AbstractConfiguration;
import org.mule.module.apikit.exception.InvalidHeaderException;
import org.mule.util.CaseInsensitiveHashMap;
import org.raml.model.Action;
import org.raml.model.parameter.Header;

import java.util.Map;

import static org.mule.module.apikit.transform.ApikitResponseTransformer.APIKIT_ROUTER_REQUEST;

public class HttpRestRequest {

    protected MuleMessage requestMessage;
    protected Action action;

    public HttpRestRequest(MuleMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public MuleMessage validate(Action action) throws MuleException {
        this.action = action;
        processHeaders();

        requestMessage.setInvocationProperty(APIKIT_ROUTER_REQUEST, "yes");
        return requestMessage;
    }

    private void processHeaders() throws InvalidHeaderException {
        for (String expectedKey : action.getHeaders().keySet()) {
            Header expected = (Header) action.getHeaders().get(expectedKey);
            Map<String, String> incomingHeaders = getIncomingHeaders(requestMessage);
            String regex;
            if (expectedKey.contains("{?}")) {
                regex = expectedKey.replace("{?}", ".*");
                for (String incoming : incomingHeaders.keySet()) {
                    String incomingValue = (String) incomingHeaders.get(incoming);
                    if ((incoming.matches(regex)) && (!expected.validate(incomingValue))) {
                        String msg = String.format("Invalid value '%s' for header %s. %s", new Object[]{incomingValue, expectedKey,
                                expected.message(incomingValue)});
                        throw new InvalidHeaderException(msg);
                    }
                }
            } else {
                String actual = (String) incomingHeaders.get(expectedKey);
                if ((actual == null) && (expected.isRequired())) {
                    throw new InvalidHeaderException("Required header " + expectedKey + " not specified");
                }
                if ((actual == null) && (expected.getDefaultValue() != null)) {
                    setHeader(expectedKey, expected.getDefaultValue());
                }
                if (actual != null) {
                    if (!expected.validate(actual)) {
                        String msg = String.format("Invalid value '%s' for header %s. %s", new Object[]{actual, expectedKey,
                                expected.message(actual)});

                        throw new InvalidHeaderException(msg);
                    }
                }
            }
        }
    }

    private void setHeader(String key, String value) {
        requestMessage.setProperty(key, value, PropertyScope.INBOUND);
        if (requestMessage.getInboundProperty("http.headers") != null) {
            ((Map) requestMessage.getInboundProperty("http.headers")).put(key, value);
        }
    }

    private Map<String,String> getIncomingHeaders(MuleMessage message)
    {

        Map<String, String> incomingHeaders = new CaseInsensitiveHashMap();
        if (message.getInboundProperty("http.headers") != null)
        {
            incomingHeaders = new CaseInsensitiveHashMap(message.<Map>getInboundProperty("http.headers"));
        }
        else
        {
            for (String key : message.getInboundPropertyNames())
            {
                if (!key.startsWith("http.")) //TODO MULE-8131
                {
                    incomingHeaders.put(key, String.valueOf(message.getInboundProperty(key)));
                }
            }
        }
        return incomingHeaders;
    }
}
