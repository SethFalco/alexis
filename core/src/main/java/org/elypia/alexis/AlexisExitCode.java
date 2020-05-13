package org.elypia.alexis;

public enum AlexisExitCode {

    NORMAL(0),

    INITIALIZATION_ERROR(1);

    private final int EXIT_CODE;

    AlexisExitCode(final int EXIT_CODE) {
        this.EXIT_CODE = EXIT_CODE;
    }

    public int getExitCode() {
        return EXIT_CODE;
    }
}
