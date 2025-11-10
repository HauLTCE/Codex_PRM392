package com.hault.codex_java.data.local.specification;

public class ArcByTitleSpecification implements Specification {
    private final String title;

    public ArcByTitleSpecification(String title) {
        this.title = "%" + title + "%";
    }

    @Override
    public String getSelectionClause() {
        return "title LIKE ?";
    }

    @Override
    public Object[] getSelectionArgs() {
        return new Object[]{title};
    }
}