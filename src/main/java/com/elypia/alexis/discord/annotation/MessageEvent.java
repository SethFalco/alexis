package com.elypia.alexis.discord.annotation;

import net.dv8tion.jda.core.entities.ChannelType;

public @interface MessageEvent {

    /**
     * @return  If a database connection is required
     *          to execute this event.
     */

    boolean requiresDatabase() default false;

    ChannelType[] scope();
}
