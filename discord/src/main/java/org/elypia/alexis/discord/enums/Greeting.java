package org.elypia.alexis.discord.enums;

import org.elypia.commandler.annotation.StringValues;

public enum Greeting {

    /** If referring to the bot saying hello. */
    @StringValues({"join", "hello"})
    WELCOME,

    /** If referring to the bot saying goodbye. */
    @StringValues({"leave", "bye", "goodbye"})
    FAREWELL
}
