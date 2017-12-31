package com.elypia.alexis.discord.commands.annotation;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptParameter {
	
	String param();
	String help();
	String defaultValue();
}
