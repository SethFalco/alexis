package com.elypia.alexis.commandler.validation;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.jdac.alias.JDACEvent;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Database.Validator.class})
public @interface Database {

    String message() default "{com.elypia.jdac.validation.Database.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Database, JDACEvent> {

        @Override
        public boolean isValid(JDACEvent value, ConstraintValidatorContext context) {
            return BotUtils.isDatabaseAlive();
        }
    }
}
