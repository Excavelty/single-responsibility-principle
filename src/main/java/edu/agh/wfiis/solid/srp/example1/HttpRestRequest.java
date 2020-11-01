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
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        List<HeaderValidationError> headerValidationErrors = validateHeaders(muleMessage, validationConstraints.getHeaderConstraints());

        if (!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }

        setMissingHeadersDefaultValuesInMessage();
        return muleMessage;
    }

    private List<HeaderValidationError> validateHeaders(MuleMessage muleMessage, List<Constraint> headerConstraints) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();
        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);
            errorMessages.addAll(validateSingleHeader(headerName, headerValue, constraint));
        }
        return errorMessages;
    }

    private List<HeaderValidationError> validateSingleHeader(String headerName, String headerValue, Constraint constraint) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();
        if (headerValue == null && constraint.isHeaderRequired()) {
            errorMessages.add(new MissingHeaderValidationError(headerName));
        }

        if (headerValue != null && !constraint.validate(headerValue)) {
            errorMessages.add(new InvalidHeaderValueValidationError(headerName, headerValue));
        }
        return errorMessages;
    }

    private void setMissingHeadersDefaultValuesInMessage() {
        for (Constraint constraint : validationConstraints.getHeaderConstraints()) {
            tryToSetDefaultHeaderValue(constraint, muleMessage);
        }
    }

    private void tryToSetDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage) {
        String constraintHeaderName = constraint.getHeaderName();
        String muleMessageHeader = muleMessage.getHeader(constraintHeaderName);
        if (muleMessageHeader == null && constraint.getDefaultValue() != null) {
            muleMessage.setHeader(constraintHeaderName, constraint.getDefaultValue());
        }
    }
}

interface HeaderValidationError {
    String getError();
}

class MissingHeaderValidationError implements HeaderValidationError {
    private final String headerName;

    public MissingHeaderValidationError(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidHeaderValueValidationError implements HeaderValidationError {
    private final String headerName;
    private final String headerValue;

    public InvalidHeaderValueValidationError(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.", headerValue, headerName);
    }
}