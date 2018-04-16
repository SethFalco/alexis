package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.Collection;

public class VoiceParser implements Parser<VoiceChannel> {

    @Override
    public VoiceChannel parse(String input) throws IllegalArgumentException {
        Collection<VoiceChannel> channels = event.getGuild().getVoiceChannels();

        for (VoiceChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }
}
