package com.elypia.alexis.commandler;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.validation.ICommandValidator;

public class DatabaseValidator implements ICommandValidator<Database> {

    @Override
    public boolean validate(CommandEvent event, Database annotation) {
        if (!BotUtils.isDatabaseAlive())
            return event.invalidate("Sorry! :c For some reason the database is down so you won't be able to perform this command right now.");

        return true;
    }

    @Override
    public String help(Database annotation) {
        return "This module requires the database, should it ever go this may be disbaled until that it resolved.";
    }
}
