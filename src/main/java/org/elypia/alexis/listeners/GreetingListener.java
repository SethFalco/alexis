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
