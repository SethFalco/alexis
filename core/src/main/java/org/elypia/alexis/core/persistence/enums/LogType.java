package org.elypia.alexis.core.persistence.enums;

/**
 * The different types of messages that users may choose to subscribe to.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 3.0.0
 */
public enum LogType {

    ANNOUCEMENTS("Get news on new features or annoucements.");

    private final String description;

    LogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
