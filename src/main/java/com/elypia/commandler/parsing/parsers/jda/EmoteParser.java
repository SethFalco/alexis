package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.entities.Emote;

import java.util.Collection;

public class EmoteParser implements Parser<Emote> {

    @Override
    public Emote parse(String input) throws IllegalArgumentException {
        Collection<Emote> emotes = event.getJDA().getEmotes();

        for (Emote emote : emotes) {
            if (emote.getId().equals(input) || emote.getAsMention().equals(input) || emote.getName().equalsIgnoreCase(input))
                return emote;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to an emote.");
    }
}
