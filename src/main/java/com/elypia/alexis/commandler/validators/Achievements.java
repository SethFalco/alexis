package com.elypia.alexis.commandler.validators;

import com.elypia.alexis.entities.data.Achievement;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Achievements {
    Achievement[] value();
    boolean invert() default false;
}
