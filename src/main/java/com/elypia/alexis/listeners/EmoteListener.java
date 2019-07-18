package com.elypia.alexis.listeners;

import com.elypia.alexis.entities.EmoteUsage;
import com.elypia.alexis.services.DatabaseService;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.Bag;
import org.hibernate.Session;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

/**
 * Count up all emotes used in guilds.
 */
@Singleton
public class EmoteListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EmoteListener.class);

    private final DatabaseService dbService;

    @Inject
    public EmoteListener(final DatabaseService dbService) {
        this.dbService = Objects.requireNonNull(dbService);

        if (dbService.isDisabled())
            logger.warn("DatabaseService is disabled, won't track or add emotes.");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (dbService.isDisabled())
            return;

        long guildId = event.getGuild().getIdLong();
        Bag<Emote> emotes = event.getMessage().getEmotesBag();

        try (Session session = dbService.open()) {
            for (Emote emote : emotes) {
                long emoteId = emote.getIdLong();
                int count = emotes.getCount(emote);
                session.save(new EmoteUsage(emoteId, guildId, count));
            }

            session.getTransaction().commit();
        }
    }
}
