package com.elypia.alexis.commandler.validation;

import com.elypia.alexis.entities.data.Achievement;
import com.elypia.jdac.alias.JDACEvent;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Achievements.Validator.class})
@Database
public @interface Achievements {

    Achievement[] value();

    boolean achieved() default true;

    String message() default "{com.elypia.jdac.validation.Achievements.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Achievements, JDACEvent> {

        private Achievement[] achievements;
        private boolean achieved;

        @Override
        public void initialize(Achievements constraintAnnotation) {
            achievements = constraintAnnotation.value();
            achieved = constraintAnnotation.achieved();
        }

        @Override
        public boolean isValid(JDACEvent value, ConstraintValidatorContext context) {
            return true;
//            MessageReceivedEvent source = (MessageReceivedEvent)value.getSource();
//            long id = source.getAuthor().getIdLong();
//            UserData data = UserData.query(id);
//
//            Set<Achievement> owned = data.getAchievements();
//            return achieved == owned.containsAll(Arrays.asList(achievements));
        }
    }
}
