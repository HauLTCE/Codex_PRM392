package com.hault.codex_java.data.local.specification;

public class ChapterByTitleSpecification implements Specification {
    private final String title;

    public ChapterByTitleSpecification(String title) {
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