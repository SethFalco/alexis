package com.elypia.alexis.discord.commands.annotation;

import java.lang.annotation.*;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	String alias();
	String help();
	Parameter[] params() default {};
	Parameter[] optParams() default {};
	ChannelType[] scope() default {};
	Permission[] permissions() default {};
}
