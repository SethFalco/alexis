package com.elypia.alexis.discord.annotation;

public @interface MessageEvent {

    /**
     * @return  If a database connection is required
     *          to execute this event.
     */

    boolean value() default false;
}
