package com.elypia.alexis.entities.data;

public enum Achievement {

    UNKNOWN("Unknown", true);

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
