package com.elypia.alexis.discord.annotations;

import com.elypia.alexis.discord.handlers.impl.CommandHandler;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {

	/**
	 * @return A list of all the alises that grant the
	 * user access to the module.
	 */

	String[] aliases();

	/**
	 * Examples: <br>
	 * <strong>&gt;ud jenni</strong>: As the defaultCommand is "define" this is
	 * the same as executing: <strong>&gt;ud define jenni</strong>.
	 *
	 * @return The default command to fallback to if no command is specified.
	 */

	String defaultCommand() default "";

	/**
	 * @return A help String to advise users what
	 * the module is for.
	 */

	String help();

	/**
	 * Submodules are what come after module in the command. <br>
	 * The format will be similar to the following: <br>
	 * <code>&gt;twitch.notify add Rheannon96</code> <br>
	 * In this case <code>notify</code> is the submodule.
	 *
	 * @return A list of modules that are directly associated
	 * with this module to be put together in help.
	 */

	Class<? extends CommandHandler>[] submodules() default {

	};
}
