package com.elypia.alexis.commandler.validators;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;

public class DatabaseValidator implements IJDACommandValidator<Database> {

    @Override
    public boolean validate(JDACommand event, Database annotation) {
        if (!BotUtils.isDatabaseAlive())
            return false;

        return true;
    }

    @Override
    public String help(Database annotation) {
        return "This module requires the database, should it ever go this may be disbaled until that it resolved.";
    }
}
