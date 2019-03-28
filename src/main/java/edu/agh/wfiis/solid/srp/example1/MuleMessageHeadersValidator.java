package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MuleMessageHeadersValidator {

    public List<ValidationResult> validate(MuleMessage headersSource, Constraints validationConstraints) {
        List<ValidationResult> results = new ArrayList<>();
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            Header toValidate = Header.of(constraint.getHeaderName(), headersSource);
            ValidationResult validationResult = validate(toValidate, constraint);
            results.add(validationResult);
        }
        return results;
    }

    private ValidationResult validate(Header toValidate, Constraint constraint){
        if (toValidate.isValueNull() && constraint.isHeaderRequired()) {
            return ValidationResult.invalid(
                    MessageFormat.format("Required header \" {0} \" not specified", toValidate.getName()));
        }

        if (!toValidate.isValueNull() && !constraint.validate(toValidate.getValue())) {
            return ValidationResult.invalid(
                    MessageFormat.format("Invalid value format for header {0}.", toValidate.getName()));
        }
        return ValidationResult.valid();
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