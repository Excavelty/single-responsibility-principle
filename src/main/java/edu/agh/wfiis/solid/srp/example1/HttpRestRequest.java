package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    private MuleMessage muleMessage;
    private Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        validateHeaders();
        setDefaultValues();
        return muleMessage;
    }

    private void validateHeaders() throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            validateHeader(constraint);
        }
    }

    private void setDefaultValues()  {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            setDefaultValue(constraint);
        }
    }

    private ValidationResult validateHeader(Constraint constraint){
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.isHeaderRequired()) {
            return ValidationResult.invalid(MessageFormat.format("Required header \" {0} \" not specified", headerName));
        }

        if (headerValue != null && !constraint.validate(headerValue)) {
            return ValidationResult.invalid(MessageFormat.format("Invalid value format for header {0}.", headerName));
        }
        return ValidationResult.valid();
    }

    private void setDefaultValue(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);

        if (headerValue == null && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
        }
    }

    public static class ValidationResult{

        private String message;

        private ValidationResult(String message){
            this.message = message;
        }

        public boolean isValid(){
            return message!=null;
        }

        public static ValidationResult  valid(){
            return new ValidationResult(null);
        }

        public static ValidationResult  invalid(String error){
            return new ValidationResult(error);
        }
    }
}