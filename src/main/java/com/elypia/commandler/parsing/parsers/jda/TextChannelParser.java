package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;

public class TextChannelParser implements Parser<TextChannel> {

    @Override
    public TextChannel parse(String input) throws IllegalArgumentException {
        Collection<TextChannel> channels = event.getGuild().getTextChannels();

        for (TextChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input) || channel.getAsMention().equals(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
