package com.elypia.alexis.entities.data;

public enum Achievement {

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
