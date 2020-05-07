/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.constraints;

import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;
import javax.persistence.EntityManager;
import javax.validation.*;
import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Database.Validator.class})
public @interface Database {

    String message() default "{org.elypia.alexis.constraints.Database.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Singleton
    class Validator implements ConstraintValidator<Database, ActionEvent<?, ?>> {

        private final EntityManager manager;

        @Inject
        public Validator(final EntityManager manager) {
            this.manager = manager;
        }

        @Override
        public boolean isValid(ActionEvent<?, ?> value, ConstraintValidatorContext context) {
            return manager.isOpen();
        }
    }
}
