package com.elypia.jdautils.annotations.command;

import com.elypia.jdautils.annotations.command.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the {@link Parameter} annotiation to be repeatable
 * so we can specify multiple parameters per command.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {

    Parameter[] value();
}
