package com.elypia.alexis.discord.annotation;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * A list of all the alises that grant the
	 * user access to this command.
	 */

	String[] aliases();

	/**
	 * A help string to advise the user of what
	 * this command does.
	 */

	String help();

	/**
	 * Should only the developers of the bot
	 * be able to do it.
	 */

	boolean developerOnly() default false;

	/**
	 * The permissions the user performing a command from
	 * this module required by default to perform any
	 * commands within this module.
	 */

	Permission[] permissions() default {

	};

	/**
	 * The parameters required for this command
	 * to function minimally.
	 */

	Parameter[] params() default {

	};

	/**
	 * The optional parameters that can help customise
	 * the experience with the command.
	 */

	OptParameter[] optParams() default {

	};

	/**
	 *
	 * @return
	 */

	String[] reactions() default {

	};

	boolean requiredDatabase() default false;
}
