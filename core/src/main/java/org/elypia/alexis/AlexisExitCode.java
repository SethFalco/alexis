package org.elypia.alexis;

public enum AlexisExitCode {

    NORMAL(0),

    INITIALIZATION_ERROR(1);

    private final int exitCode;

    AlexisExitCode(final int exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
