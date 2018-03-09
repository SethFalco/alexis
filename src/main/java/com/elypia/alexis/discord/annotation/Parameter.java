package com.elypia.alexis.discord.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

	/**
	 * The name to display this parameter as.
	 */

	String param();

	/**
	 * A small description of what the parameter
	 * is or what to get it.
	 */

	String help();

	/**
	 * The datatype that this parameter must be.
	 */

	Class<?> type();

	/**
	 * If this parameter is sensitive and should be hidden
	 * as much as possible in logs, or message should be
	 * deleted on Discord.
	 */

	boolean hidden() default false;
}
