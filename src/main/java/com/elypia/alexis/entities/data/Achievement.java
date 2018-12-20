package com.elypia.alexis.entities.data;

public enum Achievement {

    UNKNOWN(null);

    /**
     * Return the SQL query that can check if the user
     * has met the requirements for this achievment.
     */
    private final String QUERY;

    Achievement(String query) {
        QUERY = query;
    }

    public String getQuery() {
        return QUERY;
    }
}
