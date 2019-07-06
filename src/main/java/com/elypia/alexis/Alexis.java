package com.elypia.alexis;

import com.elypia.alexis.config.*;
import com.elypia.alexis.database.DatabaseService;
import com.elypia.commandler.*;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.disco.DiscordController;
import lavalink.client.io.jda.JdaLavalink;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.cli.*;
import org.slf4j.*;

import java.security.GeneralSecurityException;

/**
 * This is the main class for the bot which initialised everything Alexis
 * depends on and connects to Discord. This does not contain any
 * actual command handling code.
 */
public class Alexis {

	private static final Logger logger = LoggerFactory.getLogger(Alexis.class);

	/**
	 * The time this application started, this is used to determine runtime statistics.
	 */
	public static final long START_TIME = System.currentTimeMillis();

	public static void main(String[] args) throws GeneralSecurityException {
		CommandLine commandLine = parseArguments(args);
		String path = commandLine.getOptionValue('c', "alexis.toml");

		logger.info("Initilising: Configuration Step");
		ConfigurationService configuration = new ConfigurationService(path);

		logger.info("Initilising: Commandler Step");

		AnnotationLoader loader = new AnnotationLoader(Alexis.class);
		Context context = new ContextLoader(loader).load().build();
		Commandler commandler = new Commandler.Builder(context)
			.build();

		logger.info("Initilising: JDA Step");
		DiscordConfig discord = configuration.getDiscordConfig();

		JDA jda = new JDABuilder(discord.getToken())
			.setStatus(OnlineStatus.IDLE)
			.setActivity(Activity.watching("Seth turn me on!"))
			.setBulkDeleteSplittingEnabled(false)
			.addEventListeners(new AlexisHandler())
			.build();

		new DiscordController(commandler.getDispatcherManager(), jda);

		if (configuration.getDebugConfig().isDatabaseEnabled())
			DatabaseService database = new DatabaseService(configuration.getDatabaseConfig());

		logger.info("Initilising: LavaLink Step");
		JdaLavalink lavalink = new JdaLavalink(
			0,
			0,
			shardId -> jda
		);
	}

	private static CommandLine parseArguments(String[] args) {
		Options options = new Options();
		options.addOption("c", "config", true, "The configuration file to use, defaults to `alexis.toml`.");
		CommandLineParser parser = new DefaultParser();

		try {
			return parser.parse(options, args);
		} catch (ParseException ex) {
			ExitStatus.FAILED_TO_PARSE_ARGUMENTS.exit(ex);
		}

		throw new IllegalStateException("This shouldn't be possible!");
	}
}
