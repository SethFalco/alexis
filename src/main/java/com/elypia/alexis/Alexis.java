package com.elypia.alexis;

import com.elypia.alexis.config.BotConfig;
import com.elypia.alexis.config.embedded.DiscordConfig;
import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.managers.DatabaseManager;
import com.elypia.commandler.ModulesContext;
import com.elypia.jdac.JDACDispatcher;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
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

	public static BotConfig config;

	/**
	 * The JDA instance used to communicate with Discord.
	 */
	public static JDA jda;

	/**
	 * The Commandler instance used to manage most of the command handling.
	 */
	public static JDAC commandler;

	private static DatabaseManager dbManager;

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		// Configuration
		config = BotConfig.load("./alexis.toml");

		// ElyScript
		ElyScripts scripts = new ElyScripts(
			config.getApplicationName(),
			config.getScriptsConfig().getId(),
			config.getScriptsConfig().getRange()
		);

		// JDAC
		YouTubeHelper youtube = new YouTubeHelper("Alexis");

		ModulesContext context = new ModulesContext();
		context.addPackage("com.elypia.alexis.commandler.modules");

		JDAC.Builder jdacBuilder = new JDAC.Builder();
		jdacBuilder.setPrefix(">");
		jdacBuilder.setContext(context);
		jdacBuilder.setWebsite("https://alexis.elypia.com/");
		jdacBuilder.setEngine(scripts);

		JDAC jdac = jdacBuilder.build();

		jdac.getParser().addPackage("com.elypia.alexis.commadnelr.parsers");
		jdac.getBuilder().addPackage("com.elypia.alexis.commandler.builders", IJDACBuilder.class);

//		jdac.addInstance(new MusicModule(youtube));
//		jdac.addInstance(new YouTubeModule(youtube));

		// JDA
		DiscordConfig discord = config.getDiscordConfig();

		new JDABuilder(discord.getToken())
			.setStatus(OnlineStatus.IDLE)
			.setGame(Game.of(Game.GameType.WATCHING, "Seth turn me on!"))
			.setBulkDeleteSplittingEnabled(false)
			.addEventListener(new JDACDispatcher(jdac))
			.addEventListener(new EventHandler())
			.build();

		if (config.getDebugConfig().isDatabaseEnabled())
			dbManager = new DatabaseManager(config.getDatabaseConfig());

		// LavaLink
//		JdaLavalink lavalink = new JdaLavalink(
//			myDiscordUserId,
//			fixedNumberOfShards,
//			shardId -> getJdaInstanceFromId(shardId)
//		);
	}

	public static DatabaseManager getDatabaseManager() {
		return dbManager;
	}
}
