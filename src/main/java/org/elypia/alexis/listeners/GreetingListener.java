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

import com.google.inject.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.services.DatabaseService;
import org.hibernate.Session;
import org.slf4j.*;

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

    public void onGuildMemberEvent(GenericGuildMemberEvent event, boolean join) {
        if (dbService.isDisabled())
            return;

        boolean bot = event.getUser().isBot();

        Guild guild = event.getGuild();

        try (Session session = dbService.open()) {
            GuildData data = session.get(GuildData.class, guild.getIdLong());
            GuildFeature feature;

            if (join && bot)
                feature = data.getFeature("BOT_JOIN_NOTIFICATION");
            else if (join)
                feature = data.getFeature("USER_JOIN_NOTIFICATION");
            else if (bot)
                feature = data.getFeature("BOT_LEAVE_NOTIFICATION");
            else
                feature = data.getFeature("USER_LEAVE_NOTIFICATION");

            if (feature.isEnabled()) {
                JDA jda = event.getJDA();
//                TextChannel channel = jda.getTextChannelById(settings.getChannel());
//                String message = settings.getMessage();
//                message = DiscordUtils.get(event, message, event);

//                channel.sendMessage(message).queue();
            }
        }
    }
}
