/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.services;

import com.google.inject.*;
import net.dv8tion.jda.api.entities.Guild;
import org.elypia.alexis.ChatBot;
import org.elypia.alexis.entities.*;
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
