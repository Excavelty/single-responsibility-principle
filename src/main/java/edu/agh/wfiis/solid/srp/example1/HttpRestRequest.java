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

    private ValidationResults assertHeadersMeetConstraints(Constraints validationConstraints) {
        ValidationResults.Builder resultsBuilder = new ValidationResults.Builder();

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                resultsBuilder.addError(new MissingHeader(headerName));
            }

            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    resultsBuilder.addError(new InvalidValueFormatInHeader(headerName, headerValue));
                }
            }
        }

        return resultsBuilder.build();
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
}

abstract class HeaderValidationError {
    protected String headerName;

    public HeaderValidationError(String headerName) { this.headerName = headerName; }
    abstract public String getErrorMessage();
}

class MissingHeader extends HeaderValidationError {
    public MissingHeader(String headerName) { super(headerName); }
    @Override
    public String getErrorMessage() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidValueFormatInHeader extends HeaderValidationError {
    private String headerValue;

    public InvalidValueFormatInHeader(String headerName, String headerValue) {
        super(headerName);
        this.headerValue = headerValue;
    }
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

    public List<HeaderValidationError> getErrorList() {return validationErrors; }

    public static class Builder {
        private List<HeaderValidationError> validationErrors = new ArrayList<>();

        public Builder addError(HeaderValidationError err) {
            this.validationErrors.add(err);
            return this;
        }

        public ValidationResults build(){
            return new ValidationResults(this);
        }
    }
}
