package com.elypia.alexis.commandler.validators.command;

import com.elypia.alexis.commandler.annotations.validation.command.Developer;
import com.elypia.alexis.utils.Config;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONArray;

public class DeveloperValidator implements ICommandValidator<Developer> {

    @Override
    public void validate(MessageEvent event, Developer annotation) throws IllegalAccessException {
        User user = event.getMessageEvent().getAuthor();
        JSONArray array = Config.getConfig("discord").getJSONArray("developers");
        boolean developer = false;

        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getLong("discord_id") == user.getIdLong())
                developer = true;
        }

        if (!developer)
            throw new IllegalAccessException("Only the developers of the bot are allowed to perform this command.");
    }
}
