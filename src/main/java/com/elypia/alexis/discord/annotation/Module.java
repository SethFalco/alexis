package com.elypia.alexis.discord.annotation;

import net.dv8tion.jda.core.entities.ChannelType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

	/**
	 * Submodules are what come after module in the command. <br>
	 * The format will be similar to the following: <br>
	 * <code>&gt;twitch.notify add Rheannon96</code> <br>
	 * In this case <code>notify</code> is the submodule.
	 *
	 * @return A list of modules that are directly associated
	 * with this module to be put together in help.
	 */

	Class<?>[] submodules() default {

	};

	/**
	 * @return A list of all the alises that grant the
	 * user access to the module.
	 */

	String[] aliases();

	/**
	 * @return A help String to advise users what
	 * the module is for.
	 */

	String help();

	/**
	 * @return The types of channels Alexis can recognise
	 * this module from. By default guild and private.
	 */

	ChannelType[] scope() default {
		ChannelType.TEXT,
		ChannelType.PRIVATE,
	};

	/**
	 * @return If alexis required access to the database to use this module.
	 */

	boolean requiresDatabase() default false;
}
