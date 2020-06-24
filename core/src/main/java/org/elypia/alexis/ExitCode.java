package org.elypia.alexis;

public enum ExitCode {

    NORMAL(0),

    INITIALIZATION_ERROR(1),

    RUNTIME_ERROR(2);

    private final int id;

    ExitCode(final int exitCode) {
        this.id = exitCode;
    }

    public int getId() {
        return id;
    }
}
