package com.elypia.alexis;

import com.elypia.alexis.commandler.AlexisMisuseHandler;
import com.elypia.alexis.config.ConfigurationService;
import com.elypia.alexis.config.embedded.DiscordConfig;
import com.elypia.alexis.database.DatabaseService;
import com.elypia.commandler.*;
import com.elypia.jdac.*;
import lavalink.client.io.jda.JdaLavalink;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This is the main class for the bot which initialised everything Alexis
 * depends on and connects to Discord. This does not contain any
 * actual command handling code however.
 */
public class Alexis {

	private static final Logger logger = LoggerFactory.getLogger(Alexis.class);

	/**
	 * The time this application started, this is used to determine runtime statistics.
	 */
	public static final long START_TIME = System.currentTimeMillis();

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		logger.info("Initilising: Configuration Step");
		ConfigurationService configuration = new ConfigurationService("alexis.toml");

		logger.info("Initilising: ElyScript Step");
		ElyScripts scripts = new ElyScripts(
			configuration.getApplicationName(),
			configuration.getScriptsConfig().getId(),
			configuration.getScriptsConfig().getRange()
		);

		logger.info("Initilising: JDAC Step");
		JDAC jdac = new JDAC.Builder()
			.setPrefix(">")
			.setContext(new Context(Alexis.class))
			.setWebsite("https://alexis.elypia.com/")
			.setEngine(scripts)
			.setMisuseHandler(new AlexisMisuseHandler())
			.build();

		logger.info("Initilising: JDA Step");
		DiscordConfig discord = configuration.getDiscordConfig();

		JDA jda = new JDABuilder(discord.getToken())
			.setStatus(OnlineStatus.IDLE)
			.setActivity(Activity.watching("Seth turn me on!"))
			.setBulkDeleteSplittingEnabled(false)
			.addEventListeners(new JDACDispatcher(jdac), new EventHandler())
			.build();

		if (configuration.getDebugConfig().isDatabaseEnabled())
			DatabaseService database = new DatabaseService(configuration.getDatabaseConfig());

		logger.info("Initilising: LavaLink Step");
		JdaLavalink lavalink = new JdaLavalink(
			0,
			0,
			shardId -> jda
		);
	}
}
