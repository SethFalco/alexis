package com.elypia.alexis.discord.entities.data;

/**
 * Whenever we do anything significant, reward the player with
 * achievement. This is for internal checking of significant events
 * rather than having to access other tables or databases and doing
 * the relevent checks as required.
 */

public enum Achievement {

    /**
     * Reward upon authenticating to a NaNoWriMo account.
     * Can be revoke if NaNoWriMo account is de-authorised.
     */

    NANO_AUTHENTICATED("nano_authenticated");

    private String databaseName;

    Achievement(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
