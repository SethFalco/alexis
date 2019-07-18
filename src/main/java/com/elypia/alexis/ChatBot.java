package com.elypia.alexis;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.elypia.alexis.configuration.DiscordConfig;
import com.elypia.alexis.listeners.*;
import com.elypia.alexis.services.ConfigurationService;
import com.elypia.cmdlrdiscord.DiscordController;
import com.elypia.commandler.*;
import com.elypia.commandler.dispatchers.StandardDispatcher;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.elypiai.common.core.RequestService;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.cli.*;
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
