package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class HttpRestRequest {

    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;

        final List<HeaderValidationError> validationErrors = validateMuleMessageHeaders(muleMessage, validationConstraints);
        System.out.println(validationErrors.stream().map(HeaderValidationError::getError).collect(Collectors.joining("\n"))); // pomyslec czy jednak nie opakowac errorow w klase i tam zrobic summarize czy cos
        if(!validationErrors.isEmpty()) throw new InvalidHeaderException(validationErrors.get(0).getError());

        Map<String, String> defaultHeaderValuesByHeaderNames = validationConstraints.getHeaderConstraints().stream().collect(Collectors.toMap(Constraint::getHeaderName, Constraint::getDefaultValue));
        setMissingHeadersDefaultValueInMuleMassageClassField(defaultHeaderValuesByHeaderNames);

        return muleMessage;
    }

    private List<HeaderValidationError> validateMuleMessageHeaders(MuleMessage muleMessage, Constraints headerValidationConstraints){
        BiPredicate<String, Constraint> isRequiredHeaderMissing = (headerValue, constraint) -> headerValue == null && constraint.isHeaderRequired();
        BiPredicate<String, Constraint> isExistingHeaderValueInvalid = (headerValue, constraint) -> headerValue != null && (!constraint.validate(headerValue));

        return headerValidationConstraints.getHeaderConstraints().stream().map(constraint -> {
            String headerName = constraint.getHeaderName();
            String headerValue = muleMessage.getHeader(headerName);

            if(isRequiredHeaderMissing.test(headerValue, constraint)){
                return new RequiredHeaderMissingError(headerName);
            }
            if(isExistingHeaderValueInvalid.test(headerValue, constraint)){
                return new InvalidHeaderValueError(headerName, headerValue);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    private void setMissingHeadersDefaultValueInMuleMassageClassField(Map<String, String> defaultHeaderValuesByHeaderNames){
        BiPredicate<String, String> shouldSetDefaultHeaderValue = (headerValue, defaultHeaderValue) -> headerValue == null && defaultHeaderValue != null;

        defaultHeaderValuesByHeaderNames.entrySet().stream()
                .filter(entry -> shouldSetDefaultHeaderValue.test(muleMessage.getHeader(entry.getKey()), entry.getValue()))
                .forEach(entry -> muleMessage.setHeader(entry.getKey(), entry.getValue()));
    }
}

interface HeaderValidationError{
    String getError();
}

class RequiredHeaderMissingError implements HeaderValidationError{

    private final String headerName;

    public RequiredHeaderMissingError(String headerName){
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return "Required header " + headerName + " not specified";
    }
}

class InvalidHeaderValueError implements  HeaderValidationError{

    private final String headerName;
    private final String headerValue;

    public InvalidHeaderValueError(String headerName,String headerValue){
        this.headerName=headerName;
        this.headerValue=headerValue;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.",headerValue, headerName);
    }
}
