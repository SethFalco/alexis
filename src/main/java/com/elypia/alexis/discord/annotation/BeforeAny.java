package com.elypia.alexis.discord.annotation;

public @interface BeforeAny {

	/*
	 * With an enum - the field with the name `value`
	 * is the default field which is set when no name is
	 * specified. This allows us to do: `@BeforeAny({"help", "leave"})`
	 */

	String[] value() default {
		"help"
	};
}
