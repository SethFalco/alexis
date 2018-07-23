package com.elypia.alexis.commandler.validators;

import com.elypia.commandler.annotations.validation.Validation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Validation
public @interface Supported {

}
