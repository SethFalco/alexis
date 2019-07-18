package com.elypia.alexis.services;

import com.elypia.alexis.ChatBot;
import com.elypia.alexis.entities.*;
import com.google.inject.*;
import net.dv8tion.jda.api.entities.Guild;
import org.hibernate.Session;
import org.slf4j.*;

import java.util.List;

/**
 * The {@link AuditService} allowed the {@link ChatBot} to log
 * messages a guilds log channel if defined.
 *
 * This is helpful for displaying important information
 * like bot actions or reporting issues or changes to the
 * guild.
 */
@Singleton
public class AuditService {

    private final static Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final DatabaseService dbService;

    @Inject
    public AuditService(final DatabaseService dbService) {
        this.dbService = dbService;

        if (dbService.isDisabled())
            logger.warn("AuditService instantiated but will not log anything as database is disabled.");
    }

    public void log(Guild guild, String log) {
        if (dbService.isDisabled())
            return;

        try (Session session = dbService.open()) {
            log(guild, session.get(GuildData.class, guild.getIdLong()), log);
        }
    }

    public void log(Guild guild, GuildData guildData, String log) {
        log(guild, guildData.getSubscriptions(), log);
    }

    public void log(Guild guild, List<LogSubscription> logs, String log) {
        log(guild, logs.get(0), log);
    }

    public void log(Guild guild, LogSubscription subscription, String log) {
        if (dbService.isDisabled())
            return;

        guild.getTextChannels().get(0).sendMessage(log).queue();
    }
}
