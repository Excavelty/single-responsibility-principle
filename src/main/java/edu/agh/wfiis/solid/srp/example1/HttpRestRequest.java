package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    @Deprecated()
    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        assertHeadersMeetConstraints(validationConstraints);
        setMissingHeadersToDefaultInternal(validationConstraints);
        return muleMessage;
    }

    public void setMissingHeadersToDefault(Constraints validationConstraints){
        setMissingHeadersToDefaultInternal(validationConstraints);
    }

    private void setMissingHeadersToDefaultInternal(Constraints validationConstraints){
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.getDefaultValue() != null) {
                muleMessage.setHeader(headerName, constraint.getDefaultValue());
            }
        }
    }

    private List<HeaderValidationError> validateHeaders(Constraints validationConstraints) {
        List<HeaderValidationError> validationErrors = new ArrayList<>();
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                validationErrors.add("Required header " + headerName + " not specified");
            }

            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    validationErrors.add(MessageFormat.format("Invalid value format for header {0}.", headerName));
                }
            }
        }

        return validationErrors;
    }
}

abstract class HeaderValidationError {
    protected String headerName;

    abstract public String getErrorMessage();
}

class MissingHeader extends HeaderValidationError {
    @Override
    public String getErrorMessage() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidValueFormatInHeader extends HeaderValidationError {
    private String headerValue;

    @Override
    public String getErrorMessage() {
        return MessageFormat.format("Invalid value {0} for header {1}.", headerValue, headerName);
    }
}

class ValidationResults {
    private final List<HeaderValidationError> validationErrors;

    private ValidationResults(Builder builder){
        this.validationErrors = builder.validationErrors;
    }

    public static class Builder {
        private List<HeaderValidationError> validationErrors = new ArrayList<>();

        public Builder addError(String header){
            this.validationErrors.add(msg);
            return this;
        }

        public ValidationResults build(){
            return new ValidationResults(this);
        }
    }
}