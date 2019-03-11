package edu.agh.wfiis.solid.srp.example1.model;

public class Constraint {

    private boolean required;

    private String pattern;

    private String defaultValue;

    private String name;

    public boolean validate(String incomingValue) {
        return pattern == null ? true : pattern.matches(incomingValue);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getHeaderName() {
        return name;
    }

    public boolean isHeaderRequired() {
        return required;
    }
}
