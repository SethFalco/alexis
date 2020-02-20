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

package org.elypia.alexis.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.alexis.utils.DiscordUtils;
import org.hibernate.Session;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

@Singleton
public class GreetingListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GreetingListener.class);

    private final DatabaseService dbService;

    @Inject
    public GreetingListener(final DatabaseService dbService) {
        this.dbService = Objects.requireNonNull(dbService);

        if (dbService.isDisabled())
            logger.warn("GreetingListener instantiated but Database isn't enabled so no greetings will be sent.");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        onGuildMemberEvent(event, true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        onGuildMemberEvent(event, false);
    }

    public void onGuildMemberEvent(GenericGuildMemberEvent event, final boolean join) {
        if (dbService.isDisabled())
            return;

        final Guild guild = event.getGuild();
        final boolean bot = event.getUser().isBot();

        try (Session session = dbService.open()) {
            GuildData data = session.get(GuildData.class, guild.getIdLong());
            GuildFeature feature;

            if (join && bot)
                feature = null;
            else if (join)
                feature = null;
            else if (bot)
                feature = null;
            else
                feature = null;

            if (feature.isEnabled()) {
                TextChannel channel = DiscordUtils.getWriteableChannel(event.getGuild());
                channel.sendMessage("updated").queue();
            }
        }
    }
}
