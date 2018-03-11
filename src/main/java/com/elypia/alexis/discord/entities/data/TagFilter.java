package com.elypia.alexis.discord.entities.data;

public enum TagFilter {

    /**
     * Regardless of the function of the tag, this will
     * <strong>ONLY</strong> be applied to channels which
     * <strong>do</strong> have this tag.
     * Essentially a whitelist.
     */

    ONLY("only"),

    /**
     * Regardless of the function of the tag, this will
     * <strong>ONLY</strong> be applied to channels which
     * <strong>do NOT</strong> have this tag.
     * Essentially a blacklist.
     */

    EXCEPT("except");

    /**
     * The name as it appears in the database.
     */

    private String databaseName;

    TagFilter(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @return The name as it appears in the database.
     */

    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Due to naming conventions, require a wrapper around
     * the {@link TagFilter#valueOf} method to match it.
     *
     * @param databaseName The name as it appears in the database.
     * @return The TagFilter with the same name.
     */

    public static TagFilter getByName(String databaseName) {
        return valueOf(databaseName.toUpperCase());
    }
}
