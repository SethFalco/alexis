package com.elypia.alexis.discord.annotation;

import net.dv8tion.jda.core.entities.ChannelType;

public @interface Event {

    /**
     * @return Which types of channels this event should execute in.
     */

    ChannelType[] scope();

    /**
     * @return If a database connection is required to execute this event.
     */

    boolean requiresDatabase() default false;
}
