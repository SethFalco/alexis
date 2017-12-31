package com.elypia.alexis.discord.commands.annotation;

import java.lang.annotation.*;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	String[] aliases();
	String help();
	Parameter[] params() default {};
	OptParameter[] optParams() default {};
	ChannelType[] scope() default {};
	Permission[] permissions() default {};
	boolean developerOnly() default false;
}
