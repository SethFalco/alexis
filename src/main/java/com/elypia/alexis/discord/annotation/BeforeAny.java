package com.elypia.alexis.discord.annotation;

public @interface BeforeAny {

	String[] exclusions() default {
		"help"
	};
}
