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

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.Presence;
import org.elypia.alexis.Alexis;
import org.slf4j.*;

import javax.inject.Singleton;

/**
 * A listener that waits for JDA to initializate and trigger the
 * {@link #onReady(ReadyEvent)} method to perform one off tasks
 * and then removes itself from the {@link ListenerAdapter} pool.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ReadyListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    /** The default activity to display after the bot is ready. */
    private static final Activity DEFAULT_ACTIVITY = Activity.watching("myself get ready!");

    /**
     * Perform some initialization work, then remove this listenering since
     * it no longer needs to receive events.
     *
     * @param event The ready event, issued when JDA is ready to receive events.
     */
    @Override
    public void onReady(ReadyEvent event) {
        JDA jda = event.getJDA();
        Presence presence = jda.getPresence();
        presence.setActivity(DEFAULT_ACTIVITY);
        presence.setStatus(OnlineStatus.ONLINE);

        long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
        String timeElapsedText = String.format("%,d", timeElapsed);
        logger.info("Time taken to launch: {}ms", timeElapsedText);

        jda.removeEventListener(this);
    }
}
