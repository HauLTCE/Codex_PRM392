package com.hault.codex_java.data.local.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndSpecification implements Specification {
    private final Specification spec1;
    private final Specification spec2;

    public AndSpecification(Specification spec1, Specification spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public String getSelectionClause() {
        return "(" + spec1.getSelectionClause() + ") AND (" + spec2.getSelectionClause() + ")";
    }

    @Override
    public Object[] getSelectionArgs() {
        List<Object> args = new ArrayList<>();
        args.addAll(Arrays.asList(spec1.getSelectionArgs()));
        args.addAll(Arrays.asList(spec2.getSelectionArgs()));
        return args.toArray();
    }
}