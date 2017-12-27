package com.elypia.alexis.discord.commands.annotation;

import java.lang.annotation.*;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {
	
	String name();
	String description();
	ChannelType[] scope() default {};
	Permission[] permissions() default {};
}
