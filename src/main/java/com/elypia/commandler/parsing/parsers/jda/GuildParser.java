package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Collection;

public class GuildParser implements Parser<Guild> {

    @Override
    public Guild parse(String input) throws IllegalArgumentException {
        Collection<Guild> guilds = event.getAuthor().getMutualGuilds();

        for (Guild g : guilds) {
            if (g.getId().equals(input) || g.getName().equalsIgnoreCase(input))
                return g;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a guild.");
    }
}
