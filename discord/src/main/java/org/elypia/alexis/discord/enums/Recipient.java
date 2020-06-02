package org.elypia.alexis.discord.enums;

import org.elypia.commandler.annotation.StringValues;

public enum Recipient {

    /** If referring to user greeting messages only. */
    @StringValues({"users", "human", "humans"})
    USER,

    /** If referring to bot greeting messages only. */
    @StringValues({"bots", "robot", "robots", "automated"})
    BOT,

    /** If referring to all greeting messages. */
    @StringValues({"all"})
    BOTH
}
