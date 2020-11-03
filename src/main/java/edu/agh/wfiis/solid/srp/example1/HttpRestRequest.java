package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;

public class HttpRestRequest {

    protected MuleMessage muleMessage;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {

        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
    
            validateHeader(constraint, headerValue);
            setMissingHeaderValueToDefault(headerName, constraint);
        }

        return muleMessage;
    }

    private void setMissingHeaderValueToDefault(String headerName, Constraint constraint) {
        if (headerValue == null && constraint.getDefaultValue() != null) 
            muleMessage.setHeader(headerName, constraint.getDefaultValue());
    }

    private void validateHeader(Constraint constraint, String headerValue) throws InvalidHeaderException {
        if (headerValue == null) {
            String errorMsg = String.format("Required header is not specified: %s", headerName);
            if (constraint.isHeaderRequired() && constraint.isHeaderRequired())
                throw new InvalidHeaderException(errorMsg);
        } else {
            String errorMsg = String.format("Invalid value format for header: %s", headerName);
            if (!constraint.validate(headerValue))
                throw new InvalidHeaderException(errorMsg);
        }
    }

    // optional API
    public void validateAllHeaders(Constraints validationConstraints) throws InvalidHeaderException {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            validateHeader(constraint, headerValue);
        }
    }

    public void fixMissingHeaders(Constraints validationConstraints) {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            setMissingHeaderValueToDefault(headerName, constraint);
        }
    }
}