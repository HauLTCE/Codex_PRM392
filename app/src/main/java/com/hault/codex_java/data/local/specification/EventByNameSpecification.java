package com.hault.codex_java.data.local.specification;

public class EventByNameSpecification implements Specification {
    private final String name;

    public EventByNameSpecification(String name) {
        this.name = "%" + name + "%";
    }

    @Override
    public String getSelectionClause() {
        return "name LIKE ?";
    }

    @Override
    public Object[] getSelectionArgs() {
        return new Object[]{name};
    }
}