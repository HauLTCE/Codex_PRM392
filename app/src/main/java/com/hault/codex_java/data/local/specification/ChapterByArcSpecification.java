package com.hault.codex_java.data.local.specification;

public class ChapterByArcSpecification implements Specification {
    private final int arcId;

    public ChapterByArcSpecification(int arcId) {
        this.arcId = arcId;
    }

    @Override
    public String getSelectionClause() {
        return "arcId = ?";
    }

    @Override
    public Object[] getSelectionArgs() {
        return new Object[]{arcId};
    }
}