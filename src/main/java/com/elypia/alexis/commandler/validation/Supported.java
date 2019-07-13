package com.elypia.alexis.commandler.validation;

import javax.validation.*;
import java.lang.annotation.*;
import java.util.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Supported.Validator.class})
public @interface Supported {

    String message() default "{com.elypia.jdac.validation.Supported.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Supported, Locale> {

        @Override
        public boolean isValid(Locale value, ConstraintValidatorContext context) {
            return ResourceBundle.getBundle("CommandlerMessages").
        }
    }
}
