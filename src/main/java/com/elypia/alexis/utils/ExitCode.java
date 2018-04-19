package com.elypia.alexis.utils;

public enum ExitCode {

    PEACEFUL(0, "Shutdown peacefully."),

    FAILED_TO_READ_CONFIG(1, "Failed to read config file."),
    FAILED_TO_INIT_BOT(2, "Failed to initalise the bot.");

    private final int CODE;
    private final String MESSAGE;

    ExitCode(int code, String message) {
        CODE = code;
        MESSAGE = message;
    }

    public int getStatusCode() {
        return CODE;
    }

    public String getMessage() {
        return MESSAGE;
    }
}
