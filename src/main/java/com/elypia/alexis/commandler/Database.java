package com.elypia.alexis.commandler;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation("./resources/commands/database.svg")
public @interface Database {

}
