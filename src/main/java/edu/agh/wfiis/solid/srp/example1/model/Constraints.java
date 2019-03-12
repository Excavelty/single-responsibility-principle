package edu.agh.wfiis.solid.srp.example1.model;

import java.util.ArrayList;
import java.util.List;

public class Constraints {

    private List<Constraint> headersConstraints = new ArrayList<>();

    public List<Constraint> getHeaderConstraints() {
        return headersConstraints;
    }

    public void add(List<Constraint> constraints) {
        headersConstraints.addAll(constraints);
    }
}