package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultMuleMessageValuesSetter {

    public void  setDefaultValues(MuleMessage muleMessageToUpdate, Constraints validationConstraints)  {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            setMuleMessageHeaderValueWithDefaultValueFromConstraintIfNeeded(muleMessageToUpdate, constraint);
        }
    }
    private void setMuleMessageHeaderValueWithDefaultValueFromConstraintIfNeeded(MuleMessage muleMessage, Constraint constraint) {
        Header header = Header.of(constraint.getHeaderName(), muleMessage);
        if (header.isValueNull() && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(header.getName(), constraint.getDefaultValue());
        }
    }

    public static class Header {
        private String headerName;
        private String headerValue;

        public Header(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        public static Header of(String name, MuleMessage muleMessage){
            return new Header(name, muleMessage.getHeader(name));
        }

        public boolean isValueNull() {
            return this.headerValue == null;
        }

        public String getName(){
            return this.headerName;
        }

        public String getValue(){
            return this.headerValue;
        }
    }
}