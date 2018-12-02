package com.elypia.alexis.commandler.validators;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ElevatedValidator implements IJDACommandValidator<Elevated> {

    private final long[] developers;

    public ElevatedValidator(final long... developers) {
        this.developers = developers;
    }

    @Override
    public boolean validate(JDACommand event, Elevated annotation) {
        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        if (!source.getChannelType().isGuild())
            return true;

        if (source.getMember().hasPermission(source.getTextChannel(), Permission.MANAGE_SERVER))
            return true;

        long id = source.getMember().getUser().getIdLong();

        for (long dev : developers) {
            if (id == dev)
                return true;
        }

        return false;
    }

    @Override
    public String help(Elevated annotation) {
        return "This module requires the database, should it ever go this may be disbaled until that it resolved.";
    }
}
