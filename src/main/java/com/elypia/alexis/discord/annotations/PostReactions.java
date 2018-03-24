package com.elypia.alexis.discord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PostReactions are the reactions to perform on a command
 * upon success. These reactions will normally have their own functionality
 * when a use performs the same.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReactions {

    /**
     * @return The list of reactions associated to perform.
     */

    String[] value();
}
