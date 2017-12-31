package com.elypia.alexis.discord.commands.annotation;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	
	String param();
	String help();
	Class<?> type();
	
	/**
	 * If this parameter is sensitive and should be hidden
	 * as much as possible in logs, or message should be
	 * deleted on Discord.
	 */
	
	boolean hidden() default false;
}
