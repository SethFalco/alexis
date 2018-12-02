package com.elypia.alexis.entities.data;

public enum Achievement {

    /**
     * A user will have this achievment if they have authenticated
     * to the NaNoWriMo account through Alexis. <br>
     * The achievement is removed if they revoke it later.
     */
    NANOWRIMO_AUTHENTICATED("NaNoWriMo Authenticated", false);

    private final String NAME;
    private final boolean HIDDEN;

    Achievement(String name, boolean hidden) {
        NAME = name;
        HIDDEN = hidden;
    }

    public String getName() {
        return NAME;
    }

    public boolean isHidden() {
        return HIDDEN;
    }
}
