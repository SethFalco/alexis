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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.utils.DiscordUtils;
import org.slf4j.*;

import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class JoinKickListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JoinKickListener.class);

    /** The format to log the new total of guilds, users, and bots. */
    private final static String GUILD_USERS_FORMAT = "%,d guilds | %,d users | %,d bots!";

    /**
     * Occurs when the chatbot itself, joins a new guild.
     * If the bot joined more than 10 minutes ago, we just ignore the event as
     * sometimes Discord just gives us the event for no reason.
     *
     * @param event GuildJoinEvent
     */
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();

        // If we didn't actually join this guild now and it's a false notification.
        if (guild.getSelfMember().getTimeJoined().isBefore(OffsetDateTime.now().minusMinutes(10)))
            return;

        String name = guild.getName();
        TextChannel channel = DiscordUtils.getWriteableChannel(guild);

        logger.info("The guild {} just invited me! ({})", name, statsMessage(event.getJDA()));

        if (channel == null) {
            logger.info("We were unable write into any of the channels of {}, so no thank you message was delivered.", name);
            return;
        }

        String message =
            "Thank you for inviting me! My default prefix is `$` but you can mention me too!\n" +
                "Feel free to try my help command!";
        channel.sendMessage(message).queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        logger.info("The guild {} just kicked me! ({})", event.getGuild().getName(), statsMessage(event.getJDA()));
    }

    /**
     * @param jda The JDA instance that has joined or left a guild.
     * @return The stats for this bot.
     */
    private String statsMessage(JDA jda) {
        int guildCount = jda.getGuilds().size();
        List<User> users = jda.getUsers();
        long botCount = users.stream().filter(User::isBot).count();
        long userCount = users.size() - botCount;

        return String.format(GUILD_USERS_FORMAT, guildCount, userCount, botCount);
    }
}
