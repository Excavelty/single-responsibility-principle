package edu.agh.solid.srp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.mule.DefaultMuleContext;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.config.DefaultMuleConfiguration;
import org.raml.model.Action;
import org.raml.model.parameter.Header;

public class HttpRestRequestTest {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private static final Action VALIDATION_CONTRACT = prepareValidationContract();

    private MuleMessage testMessage;

    @org.junit.Before
    public void setUp() throws Exception {
        testMessage = prepareEmptyTestMessage();
    }


    @org.junit.Test
    public void validate() throws Exception {
        testMessage.setProperty("http.headers", Maps.newHashMap(ImmutableMap.of(CONTENT_TYPE_HEADER_NAME, "application/json")), PropertyScope.INBOUND);
        new HttpRestRequest(testMessage).validate(VALIDATION_CONTRACT);
    }

    private MuleMessage prepareEmptyTestMessage() {
        DefaultMuleContext testContext = new DefaultMuleContext();
        testContext.setMuleConfiguration(new DefaultMuleConfiguration());
        return new DefaultMuleMessage("", testContext);
    }

    private static Action prepareValidationContract() {
        Header contentType = new Header();
        contentType.setDisplayName(CONTENT_TYPE_HEADER_NAME);
        contentType.setRequired(true);

        Header accept = new Header();
        accept.setDisplayName(ACCEPT_HEADER_NAME);
        accept.setDefaultValue("application/json");

        Action action = new Action();
        action.setHeaders(ImmutableMap.<String, Header>of(CONTENT_TYPE_HEADER_NAME, contentType, ACCEPT_HEADER_NAME, accept));
        return action;
    }

}