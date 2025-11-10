package com.hault.codex_java.data.local.specification;

public class WorldByNameSpecification implements Specification {
    private final String name;

    public WorldByNameSpecification(String name) {
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