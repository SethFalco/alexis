package org.elypia.alexis.binders;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.repositories.GuildRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.Request;
import org.elypia.commandler.api.HeaderBinder;

import javax.inject.*;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class GuildBinder implements HeaderBinder {

    /** To select data from the database. */
    private final GuildRepository guildRepo;

    @Inject
    public GuildBinder(final GuildRepository guildRepo) {
        this.guildRepo = guildRepo;
    }

    @Override
    public <S, M> Map<String, String> bind(Request<S, M> request) {
        Event source = (Event)request.getSource();
        Guild guild = EventUtils.getGuild(source);

        if (guild == null)
            return null;

        GuildData data = guildRepo.selectGuild(guild.getIdLong());
        HashMap<String, String> headers = new HashMap<>();
        headers.put("GUILD_PREFIX", data.getPrefix());

        return headers;
    }
}
