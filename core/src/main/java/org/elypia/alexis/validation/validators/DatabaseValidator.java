package org.elypia.alexis.validation.validators;

import org.elypia.alexis.validation.constraints.Database;
import org.elypia.commandler.event.ActionEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.*;

@ApplicationScoped
public class DatabaseValidator implements ConstraintValidator<Database, ActionEvent<?, ?>> {

    @Inject
    private EntityManager manager;

    @Override
    public boolean isValid(ActionEvent<?, ?> value, ConstraintValidatorContext context) {
        return manager.isOpen();
    }
}
