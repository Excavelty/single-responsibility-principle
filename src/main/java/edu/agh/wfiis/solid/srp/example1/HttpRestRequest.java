package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    private MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        validateHeaders(validationConstraints);
        setHeadersDefaultValues(validationConstraints);
        return muleMessage;
    }

    private void setHeadersDefaultValues(Constraints validationConstraints) {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            setHeaderDefaultValue(constraint);
        }
    }

    private void validateHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            validateHeader(constraint);
        }
    }

    private void setHeaderDefaultValue(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);
        setHeaderDefaultValue(constraint, headerName, headerValue);
    }

    private ValidationResult validateHeader(Constraint constraint) throws InvalidHeaderException {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (constraint.isHeaderRequired() && validateIfHeaderPresent(headerValue)) {
           return  ValidationResult.invalid("Required header " + headerName + " not specified");
        }

        if (headerValue != null && !constraint.validate(headerValue)) {
                return ValidationResult.invalid(MessageFormat.format("Invalid value format for header {0}.", headerName));
        }

        return ValidationResult.valid();
    }

    private boolean validateIfHeaderPresent(String headerValue) {
        return headerValue != null && !headerValue.isEmpty();
    }

    public static class ValidationResult {

        private String errorMessage;

        private ValidationResult(String errorMessage){
            this.errorMessage = errorMessage;
        }

        public boolean isValid(){
            return errorMessage == null;
        }

        public static ValidationResult valid(){
            return new ValidationResult(null);
        }

        public static ValidationResult invalid(String errorMessage){
            return new ValidationResult(errorMessage);
        }

    }

    private void setHeaderDefaultValue(Constraint constraint, String headerName, String headerValue) {
        if (headerValue == null && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
        }
    }
}