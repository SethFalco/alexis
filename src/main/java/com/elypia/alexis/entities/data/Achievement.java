package com.elypia.alexis.entities.data;

public enum Achievement {

    NANOWRIMO_AUTHENTICATED("NaNoWriMo Authenticated");

    private final String NAME;

    Achievement(String name) {
        NAME = name;
    }

    public String getName() {
        return NAME;
    }
}
