package com.elypia.alexis.validation;

import com.elypia.alexis.utils.Language;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Supported.Validator.class})
public @interface Supported {

    String message() default "{com.elypia.jdac.validation.Supported.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Supported, Language> {

        @Override
        public boolean isValid(Language value, ConstraintValidatorContext context) {
            return true;
//            return Alexis.supportedLanguages.contains(language);
        }
    }
}
