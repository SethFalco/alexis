package com.elypia.alexis.commandler.validators.command;

import com.elypia.alexis.commandler.annotations.validation.command.Developer;
import com.elypia.alexis.utils.Config;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONArray;

public class DeveloperValidator implements ICommandValidator<Developer> {

    @Override
    public boolean validate(MessageEvent event, Developer annotation) {
        User user = event.getMessage().getAuthor();
        JSONArray array = Config.getConfig("discord").getJSONArray("developers");

        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getLong("discord_id") == user.getIdLong())
                return true;
        }

        return event.invalidate("Only the developers of the bot are allowed to perform this command.");
    }

    @Override
    public String help(Developer annotation) {
        return "Only the developers of the bot can perform this command.";
    }
}
