package com.elypia.alexis.discord.annotation;

import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

	/**
	 * A list of all the alises that grant the
	 * user access to the module.
	 */

	String[] aliases();

	/**
	 * A help String to advise users what
	 * the module is for.
	 */

	String help();

	/**
	 * A list of modules that are directly associated
	 * with this module to be put together in help.
	 */

	Class<?>[] submodules() default {

	};

	/**
	 * The types of channels Alexis can recognise
	 * this module from. By default guild and private.
	 */

	ChannelType[] scope() default {
		ChannelType.TEXT,
		ChannelType.PRIVATE,
	};

	boolean requiresDatabase() default false;
}
