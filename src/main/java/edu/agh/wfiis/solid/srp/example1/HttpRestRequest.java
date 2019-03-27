package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    private MuleMessage muleMessage;
    private Constraints validationConstraints;
    private Header header;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) {
        this.validationConstraints = validationConstraints;
        validateHeaders();
        setDefaultValues();
        return muleMessage;
    }

    private void validateHeaders() {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            loadHeader(constraint);
            ValidationResult validationResult = validateHeader(constraint);
            dealWithValidationResults(validationResult);
        }
    }

    private void setDefaultValues()  {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            setDefaultValue(constraint);
        }
    }

    private void loadHeader(Constraint constraint) {
        String headerName = constraint.getHeaderName();
        String headerValue = muleMessage.getHeader(headerName);
        this.header = new Header(headerName, headerValue);
    }

    private ValidationResult validateHeader(Constraint constraint){
        if (header.isValueNull() && constraint.isHeaderRequired()) {
            return ValidationResult.invalid(
                    MessageFormat.format("Required header \" {0} \" not specified", header.getName()));
        }

        if (!header.isValueNull() && !constraint.validate(header.getValue())) {
            return ValidationResult.invalid(
                    MessageFormat.format("Invalid value format for header {0}.", header.getName()));
        }
        return ValidationResult.valid();
    }

    private void dealWithValidationResults(ValidationResult validationResult){
        if (!validationResult.isValid()) {
            validationResult.printMessage();
        }
    }

    private void setDefaultValue(Constraint constraint) {
        if (header.isValueNull() && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(header.getName(), constraint.getDefaultValue());
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

        public void printMessage(){
            System.out.println(message);
        }
    }

    public class Header {
        private String headerName;
        private String headerValue;

        public Header(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
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