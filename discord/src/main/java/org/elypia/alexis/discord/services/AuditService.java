/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.services;

import net.dv8tion.jda.api.entities.Guild;
import org.elypia.alexis.Alexis;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.repositories.GuildRepository;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * The {@link AuditService} allowed the {@link Alexis} to log
 * messages a guilds log channel if defined.
 *
 * This is helpful for displaying important information
 * like bot actions or reporting issues or changes to the
 * guild.
 */
@Singleton
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final GuildRepository guildRepo;

    @Inject
    public AuditService(final GuildRepository guildRepo) {
        this.guildRepo = Objects.requireNonNull(guildRepo);
    }

    public void log(Guild guild, String log) {
        log(guild, guildRepo.findBy(guild.getIdLong()), log);
    }

    public void log(Guild guild, GuildData guildData, String log) {
//        log(guild, guildData.getSubscriptions(), log);
    }

    public void log(Guild guild, List<LogSubscription> logs, String log) {
        log(guild, logs.get(0), log);
    }

    public void log(Guild guild, LogSubscription subscription, String log) {
        guild.getTextChannels().get(0).sendMessage(log).queue();
    }
}
