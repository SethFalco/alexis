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

package org.elypia.alexis.discord;

import java.util.Collection;
import java.util.EnumSet;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

import org.elypia.alexis.core.ExitCode;
import org.elypia.alexis.discord.listeners.ConnectionListener;
import org.elypia.alexis.discord.listeners.EmoteListener;
import org.elypia.alexis.discord.listeners.GreetingListener;
import org.elypia.alexis.discord.listeners.JoinLeaveListener;
import org.elypia.alexis.discord.listeners.LoggingListener;
import org.elypia.comcord.configuration.DiscordConfig;
import org.elypia.retropia.core.HttpClientSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class DiscordBot {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    /** The default {@link Activity} to display while the bot launches. */
    private static final Activity DEFAULT_ACTIVITY = Activity.watching("myself launch!");

    /** A list of {@link GatewayIntent} the bot will use by default. */
    private static final Collection<GatewayIntent> DEFAULT_INTENTS = EnumSet.of(
        GatewayIntent.GUILD_MEMBERS,
        GatewayIntent.GUILD_EMOJIS,
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.DIRECT_MESSAGE_REACTIONS
    );

    private static final Collection<CacheFlag> CACHE_FLAGS_ENABLED = EnumSet.of(
        CacheFlag.ACTIVITY
    );

    /** The Discord client, this lets us interact with Discords API. */
    private JDA jda;

    @Inject
    public DiscordBot(DiscordConfig discordConfig, ConnectionListener connectionListener, LoggingListener loggingListener, EmoteListener emoteListener, GreetingListener greetingListener, JoinLeaveListener joinLeaveListener) throws LoginException {
        String token = discordConfig.getBotToken();

        if (token == null) {
            logger.error("No Discord bot token was provided to the application.");
            System.exit(ExitCode.INITIALIZATION_ERROR.getId());
        }

        logger.info("Initializing JDA and authenticating to Discord.");

        try {
            jda = JDABuilder.create(token, DEFAULT_INTENTS)
                .enableCache(CACHE_FLAGS_ENABLED)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setStatus(OnlineStatus.IDLE)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(DEFAULT_ACTIVITY)
                .setHttpClient(HttpClientSingleton.getClient())
                .addEventListeners(
                    connectionListener,
                    loggingListener,
                    emoteListener,
                    greetingListener,
                    joinLeaveListener
                )
                .build();
        } catch (Exception ex) {
            logger.error("Failed to authenticated to the Discord API.", ex);
            System.exit(ExitCode.INITIALIZATION_ERROR.getId());
        }
    }

    @PreDestroy
    public void preDestroy() {
        jda.shutdown();
    }

    @ApplicationScoped
    @Produces
    public JDA getJda() {
        return jda;
    }
}
