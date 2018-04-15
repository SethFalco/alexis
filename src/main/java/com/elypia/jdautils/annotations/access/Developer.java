package com.elypia.jdautils.annotations.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Modules or commands annotiated with @Developer
 * can only be performed by the developers as dictated by the
 * config file upon initialising the chatbot.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Developer {

}
