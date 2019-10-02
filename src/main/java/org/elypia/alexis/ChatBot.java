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

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.cli.*;
import org.elypia.alexis.configuration.DiscordConfig;
import org.elypia.alexis.listeners.*;
import org.elypia.alexis.services.ConfigurationService;
import org.elypia.comcord.DiscordController;
import org.elypia.commandler.*;
import org.elypia.commandler.dispatchers.StandardDispatcher;
import org.elypia.commandler.loaders.AnnotationLoader;
import org.elypia.commandler.metadata.ContextLoader;
import org.elypia.elypiai.common.core.RequestService;
import org.slf4j.*;

import java.security.GeneralSecurityException;

/**
 * This is the main class for the bot which initialised everything ChatBot
 * depends on and connects to Discord. This does not contain any
 * actual command handling code.
 */
public class ChatBot {

	private static final Logger logger = LoggerFactory.getLogger(ChatBot.class);

	/** The time this application started, this is used to determine runtime statistics. */
	public static final long START_TIME = System.currentTimeMillis();

	public static void main(String[] args) throws GeneralSecurityException {
		logger.info("Application launched, parsing CLI arguments.");
		Options options = new Options();
		options.addOption("c", "config", true, "The configuration file to use, defaults to `alexis.toml`.");
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;

		try {
			commandLine = parser.parse(options, args, false);
		} catch (ParseException ex) {
			logger.error(ex.getMessage());
			System.exit(1);
		}

		logger.info("Load configuration service from configuration file.");
		String path = commandLine.getOptionValue('c', "alexis.toml");
		FileConfig fileConfig = FileConfig.of(path);
		fileConfig.load();
		ConfigurationService configuration = new ObjectConverter().toObject(fileConfig, ConfigurationService::new);

		logger.info("Initialize this Commandler application.");
		AnnotationLoader loader = new AnnotationLoader(ChatBot.class.getPackage());
		Context context = new ContextLoader(loader).load().build();

		Commandler commandler = new Commandler.Builder(context)
			.addModules(new ChatBotModule(commandLine, configuration))
			// TODO: Specify Commandler, or make a way to not require Commandler to be specified.
			.addDispatchers(new StandardDispatcher(null, ">"))
			.build();

		logger.info("Initialize JDA and authenticate to Discord.");
		DiscordConfig discord = configuration.getDiscord();

		// TODO: Add a real status from a database.
		JDA jda = new JDABuilder(discord.getToken())
			.setStatus(OnlineStatus.IDLE)
			.setBulkDeleteSplittingEnabled(false)
			.setHttpClient(RequestService.getBuilder().build())
			.setActivity(Activity.watching("Seth turn me on!"))
			// TODO: Somehow pass the dependencies in
			.addEventListeners(
				new EmoteListener(null),
				new MiscListener(configuration.getDiscord()),
				new GreetingListener(null),
				new XpListener(null, null)
			)
			.build();

		new DiscordController(commandler.getDispatcherManager(), jda);
	}
}
