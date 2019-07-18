package com.elypia.alexis.constraints;

import com.elypia.alexis.services.DatabaseService;
import com.elypia.commandler.CommandlerEvent;
import com.google.inject.Inject;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Database.Validator.class})
public @interface Database {

    String message() default "{com.elypia.jdac.constraints.Database.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Database, CommandlerEvent<?>> {

        private final DatabaseService dbService;

        @Inject
        public Validator(final DatabaseService dbService) {
            this.dbService = dbService;
        }

        @Override
        public boolean isValid(CommandlerEvent<?> value, ConstraintValidatorContext context) {
            return dbService.isEnabled();
        }
    }
}
