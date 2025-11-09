package com.hault.codex_java.data.local.specification;

/**
 * Represents a specification that can be converted into a SQL query clause.
 */
public interface Specification {
    /**
     * Returns the SQL WHERE clause for this specification.
     * e.g., "name LIKE ? AND worldId = ?"
     *
     * @return A SQL selection string.
     */
    String getSelectionClause();

    /**
     * Returns the arguments for the placeholders in the selection clause.
     *
     * @return An array of Objects for binding.
     */
    Object[] getSelectionArgs();
}