package com.elypia.alexis.discord.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Parameters.class)
public @interface Parameter {

	/**
	 * The name to display this parameter as.
	 */

	String name();

	/**
	 * A small description of what the parameter is.
	 */

	String help();

	/**
	 * If this parameter is sensitive and should be hidden
	 * as much as possible in the logs, database, or messages.
	 */

	boolean secret() default false;
}
