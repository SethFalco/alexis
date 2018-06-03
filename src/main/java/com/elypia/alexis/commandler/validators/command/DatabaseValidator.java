package com.elypia.alexis.commandler.validators.command;

import com.elypia.alexis.commandler.annotations.validation.command.Database;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;

public class DatabaseValidator implements ICommandValidator<Database> {

    @Override
    public void validate(MessageEvent event, Database annotation) throws IllegalAccessException {
        if (BotUtils.isDatabaseAlive())
            return;

        String message = "Sorry! :c For some reason the database is down so you won't be able to perform this command right now.";
        throw new IllegalAccessException(message);
    }
}
