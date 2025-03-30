/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.elypia.alexis.core.i18n.AlexisMessages;
import org.elypia.alexis.core.persistence.entities.GuildData;
import org.elypia.alexis.core.persistence.repositories.GuildRepository;
import org.elypia.alexis.discord.DiscordBot;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.comcord.ActivatedListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

/**
 * Listener for when the {@link DiscordBot} joins or leaves
 * guilds.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Singleton
public class JoinLeaveListener extends ActivatedListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JoinLeaveListener.class);

    /** The format to log the new total of guilds, users, and bots. */
    private static final String GUILD_USERS_FORMAT = "%,d guilds | %,d users | %,d bots!";

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public JoinLeaveListener(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

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

        if (isOutdatedEvent(guild))
            return;

        String name = guild.getName();
        TextChannel channel = DiscordUtils.getWriteableChannel(guild);

        if (logger.isInfoEnabled())
            logger.info("The guild `{}` just invited me! ({})", name, statsMessage(event.getJDA()));

        if (channel == null) {
            logger.info("We were unable to talk in any channel in `{}`; no thank you message was delivered.", name);
            return;
        }

        String message = messages.thankYouForInvite();
        channel.sendMessage(message).queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();

        if (logger.isInfoEnabled())
            logger.info("The guild {} just kicked me! ({})", guild.getName(), statsMessage(event.getJDA()));

        Optional<GuildData> optGuildData = guildRepo.findOptionalBy(guild.getIdLong());

        optGuildData.ifPresent((guildData) -> {
            Duration duration = guildData.getDataRetentionDuration();

           if (duration != null && !duration.isZero()) {
               logger.info("The guild that kicked me has a data retention duration configured, not deleting any data yet.");
               return;
           }

            logger.info("The guild that kicked me didn't have a data retention duration configured, deleting all guild data.");
            guildRepo.remove(guildData);
        });
    }

    /**
     * @param jda The JDA instance that has joined or left a guild.
     * @return The stats for this bot.
     */
    private String statsMessage(JDA jda) {
        int guildCount = jda.getGuilds().size();
        List<User> users = jda.getUsers();
        long botCount = users.stream().filter(User::isBot).count();

        return String.format(GUILD_USERS_FORMAT, guildCount, users.size(), botCount);
    }

    /**
     * Sometimes Discord is dumb and gives events for guild joins that had already
     * occured. In the scenario that this occurs, we check and see did we join the guild
     * less than 10 minutes ago? If we've been around longer, that's probably Discord
     * being a dummy and giving us a false event.
     *
     * @param guild The guild to check against.
     * @return If this is an old event.
     */
    private boolean isOutdatedEvent(Guild guild) {
        return guild.getSelfMember().getTimeJoined().isBefore(OffsetDateTime.now().minusMinutes(10));
    }
}
