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

package org.elypia.alexis;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.elypia.alexis.listeners.*;
import org.elypia.comcord.DiscordConfig;
import org.elypia.elypiai.common.core.RequestService;
import org.slf4j.*;

import javax.enterprise.inject.Produces;
import javax.inject.*;
import javax.security.auth.login.LoginException;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class DiscordBot {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    /** The default {@link Activity} to display while the bot launches. */
    private static final Activity DEFAULT_ACTIVITY = Activity.watching(" myself launch!");

    /** The Discord client, this lets us interact with Discords API. */
    private final JDA jda;

    @Inject
    public DiscordBot(
        DiscordConfig discordConfig,
        ReadyListener readyListener,
        EmoteListener emoteListener,
        EntityListener entityListener,
        GreetingListener greetingListener,
        JoinKickListener joinKickListener,
        XpListener xpListener
    ) throws LoginException {
        logger.info("Initialize JDA and authenticate to Discord.");
        String token = discordConfig.getBotToken();

        jda = new JDABuilder(token)
            .setStatus(OnlineStatus.IDLE)
            .setBulkDeleteSplittingEnabled(false)
            .setActivity(DEFAULT_ACTIVITY)
            .setHttpClient(RequestService.getBuilder().build())
            .addEventListeners(
                readyListener,
                emoteListener,
                entityListener,
                greetingListener,
                joinKickListener,
                xpListener
            )
            .build();
    }

    @Produces
    public JDA getJda() {
        return jda;
    }
}
