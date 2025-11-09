package com.hault.codex_java.data.local.specification;

public class CharacterByWorldSpecification implements Specification {
    private final int worldId;

    public CharacterByWorldSpecification(int worldId) {
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