package com.elypia.alexis.commandler.dyndefault;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.interfaces.DynDefaultValue;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;

public class CurrentGuild implements DynDefaultValue {

    @Override
    public String defaultValue(CommandlerEvent<?> commandlerEvent) {
        GenericGuildEvent source = (GenericGuildEvent)commandlerEvent.getSource();
        return source.getGuild().getId();
    }
}
