/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.constraints;

import org.elypia.alexis.services.DatabaseService;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import javax.validation.*;
import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Database.Validator.class})
public @interface Database {

    String message() default "{org.elypia.jdac.constraints.Database.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Database, ActionEvent<?, ?>> {

        private final DatabaseService dbService;

        @Inject
        public Validator(final DatabaseService dbService) {
            this.dbService = dbService;
        }

        @Override
        public boolean isValid(ActionEvent<?, ?> value, ConstraintValidatorContext context) {
            return dbService.isEnabled();
        }
    }
}
