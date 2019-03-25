package com.elypia.alexis.utils;

import org.slf4j.*;

public enum ExitCode {

    PEACEFUL(0, "Shutdown peacefully."),

    FAILED_TO_READ_CONFIG(1, "Failed to read configurationService file."),
    FAILED_TO_INIT_BOT(2, "Failed to initalise the bot.");

    private static final Logger logger = LoggerFactory.getLogger(ExitCode.class);

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

    public void exit() {
        if (CODE == 0)
            logger.trace(MESSAGE);
        else
            logger.error(MESSAGE);

        System.exit(CODE);
    }
}
