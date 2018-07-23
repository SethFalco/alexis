package com.elypia.alexis.commandler.validators;

import com.elypia.alexis.entities.data.Achievement;
import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Achievements {
    Achievement[] value();
    boolean invert() default false;
}
