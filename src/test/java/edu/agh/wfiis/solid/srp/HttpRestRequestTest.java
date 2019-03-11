package edu.agh.wfiis.solid.srp;


import edu.agh.wfiis.solid.srp.example1.HttpRestRequest;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.Constraint;

import java.util.HashMap;

public class HttpRestRequestTest {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private static final Constraints VALIDATION_CONTRACT = prepareValidationContract();

    private MuleMessage testMessage;

    @org.junit.Before
    public void setUp() throws Exception {
        testMessage = new MuleMessage();
    }

    @org.junit.Test
    public void validate() throws Exception {
        testMessage.setInboundProperty("http.headers", new HashMap<String, String>() {{
            put(CONTENT_TYPE_HEADER_NAME, "application/json");
        }});
        new HttpRestRequest(testMessage).validate(VALIDATION_CONTRACT);
    }

    private static Constraints prepareValidationContract() {
        Constraint contentType = new Constraint();
        contentType.setDisplayName(CONTENT_TYPE_HEADER_NAME);
        contentType.setRequired(true);

        Constraint accept = new Constraint();
        accept.setDisplayName(ACCEPT_HEADER_NAME);
        accept.setDefaultValue("application/json;q=0.9,*/*;q=0.8");

        Constraints constraints = new Constraints();
        constraints.setHeaders(new HashMap<String, Constraint>() {{
            put(CONTENT_TYPE_HEADER_NAME, contentType);
            put(ACCEPT_HEADER_NAME, accept);
        }});
        return constraints;
    }

}