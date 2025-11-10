package com.hault.codex_java.data.local.specification;

public class ArcByWorldSpecification implements Specification {
    private final int worldId;

    public ArcByWorldSpecification(int worldId) {
        this.worldId = worldId;
    }

    @Override
    public String getSelectionClause() {
        return "worldId = ?";
    }

    @Override
    public Object[] getSelectionArgs() {
        return new Object[]{worldId};
    }
}