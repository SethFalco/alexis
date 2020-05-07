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

package org.elypia.alexis.discord.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.discord.utils.DiscordUtils;
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
