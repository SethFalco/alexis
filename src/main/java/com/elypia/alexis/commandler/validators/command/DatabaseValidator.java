package com.elypia.alexis.commandler.validators.command;

import com.elypia.alexis.commandler.annotations.validation.command.Database;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;

public class DatabaseValidator implements ICommandValidator<Database> {

    @Override
    public boolean validate(MessageEvent event, Database annotation) {
        if (BotUtils.isDatabaseAlive())
            return event.invalidate("Sorry! :c For some reason the database is down so you won't be able to perform this command right now.");

        return true;
    }

    @Override
    public String help(Database annotation) {
        return "This module required the database, should it ever go this may be disbaled until that it resolved.";
    }
}
