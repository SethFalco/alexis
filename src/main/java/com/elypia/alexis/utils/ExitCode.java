package com.elypia.alexis.utils;

public enum ExitCode {

    PEACEFUL(0, "Shutdown peacefully."),

    GENERATED_NEW_CONGIG(1, "Generated a new config file at: "),
    FAILED_TO_READ_CONFIG(2, "Failed to read config file at: "),
    FAILED_TO_WRITE_CONFIG(3, "Failed to write file to: "),
    UNKNOWN_CONFIG_ERROR(4, "Unknown config error."),
    FAILED_TO_INIT_BOT(5, "Failed to initalise the bot."),

    MALFORMED_CONFIG_DISCORD(20, "Config does not specify Discord object. Consider regenerating the configuration."),
    MALFORMED_CONFIG_TOKEN(21, "Config does not specify Discord bot token. Consider regenerating the configuration."),
    MALFORMED_CONFIG_PREFIX(22, "Config does not specify a default prefix. Consider regenerating the configuration.");

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
