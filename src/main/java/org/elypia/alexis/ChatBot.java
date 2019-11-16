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

package org.elypia.alexis;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.elypia.alexis.listeners.*;
import org.elypia.comcord.DiscordConfig;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.injection.InjectorService;
import org.elypia.elypiai.common.core.RequestService;
import org.slf4j.*;

import java.security.GeneralSecurityException;

/**
 * This is the main class for the bot which initialised everything ChatBot
 * depends on and connects to Discord. This does not contain any
 * actual command handling code.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class ChatBot {

	private static final Logger logger = LoggerFactory.getLogger(ChatBot.class);

	/** The time this application started, this is used to determine runtime statistics. */
	public static final long START_TIME = System.currentTimeMillis();

	private static final Activity DEFAULT_ACTIVITY = Activity.watching(" myself turn on.");

	public static void main(String[] args) throws GeneralSecurityException {
		logger.info("Initialize the Commandler application.");

		Commandler commandler = new Commandler();
		InjectorService injector = commandler.getAppContext().getInjector();

		logger.info("Initialize JDA and authenticate to Discord.");
		String token = injector.getInstance(DiscordConfig.class).getBotToken();

		JDA jda = new JDABuilder(token)
			.setStatus(OnlineStatus.IDLE)
			.setBulkDeleteSplittingEnabled(false)
			.setActivity(DEFAULT_ACTIVITY)
			.setHttpClient(RequestService.getBuilder().build())
			.addEventListeners(
				injector.getInstance(EmoteListener.class),
				injector.getInstance(EntityListener.class),
				injector.getInstance(GreetingListener.class),
				injector.getInstance(JoinKickListener.class),
				injector.getInstance(TaskListener.class),
				injector.getInstance(XpListener.class)
			)
			.build();

		injector.add(jda, JDA.class);
		commandler.run();
	}
}
