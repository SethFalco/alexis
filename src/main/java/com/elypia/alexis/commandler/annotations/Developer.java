package com.elypia.alexis.commandler.annotations;

import java.lang.annotation.*;

/**
 * Modules or commands annotiated with @Developer
 * can only be performed by the developers as dictated by the
 * config file upon initialising the chatbot.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Developer {

}
