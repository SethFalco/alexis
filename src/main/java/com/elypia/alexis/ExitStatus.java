package com.elypia.alexis;

import org.slf4j.*;

public enum ExitStatus {

    // Graceful Exit
    PEACEFUL(0, "Shutdown peacefully."),

    // Failed to Initialize Application
    FAILED_TO_PARSE_ARGUMENTS(1, "Failed to parse arguments."),
    FAILED_TO_READ_CONFIG(2, "Failed to read configuration file."),
    FAILED_TO_INIT_BOT(3, "Failed to initalise the bot.");

    private static final Logger logger = LoggerFactory.getLogger(ExitStatus.class);

    private final int CODE;
    private final String MESSAGE;

    ExitStatus(int code, String message) {
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
        if (CODE != 0)
            throw new IllegalStateException("Non-zero status should exit with the exception argument.");

        logger.trace(MESSAGE);
        System.exit(CODE);
    }

    public void exit(Exception ex) {
        logger.error(MESSAGE, ex);
        System.exit(CODE);
    }
}
