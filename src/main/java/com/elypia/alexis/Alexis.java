package com.elypia.alexis;

import com.elypia.alexis.commandler.builders.*;
import com.elypia.alexis.commandler.parsers.LanguageParser;
import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.config.BotConfig;
import com.elypia.alexis.config.embedded.*;
import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.handlers.EventHandler;
import com.elypia.alexis.handlers.modules.*;
import com.elypia.alexis.handlers.modules.discord.*;
import com.elypia.alexis.handlers.modules.gaming.*;
import com.elypia.alexis.handlers.modules.media.*;
import com.elypia.alexis.handlers.modules.settings.*;
import com.elypia.alexis.managers.DatabaseManager;
import com.elypia.alexis.utils.AlexisConfiler;
import com.elypia.commandler.jda.JDACommandler;
import com.elypia.elypiai.nanowrimo.NanoUser;
import com.elypia.elypiai.osu.OsuPlayer;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.utils.Language;
import com.elypia.elyscript.*;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.*;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.security.GeneralSecurityException;

/**
 * This is the main class for the bot which initialised everything Alexis
 * depends on and connects to Discord. This does not contain any
 * actual command handling code however.
 */
public class Alexis {

	/**
	 * The time this application started, this is used to determine runtime statistics.
	 */
	public static final long START_TIME = System.currentTimeMillis();

	private static final Logger logger = LoggerFactory.getLogger(Alexis.class);

	public static BotConfig config;

	/**
	 * The JDA instance used to communicate with Discord.
	 */
	public static JDA jda;

	/**
	 * The Commandler instance used to manage most of the command handling.
	 */
	public static JDACommandler commandler;

	/**
	 * The ElyScript store to validate ElyScript and store scripts in order to
	 * query as required.
	 */
	public static ScriptStore scripts;

	private static DatabaseManager dbManager;

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		config = BotConfig.load("./alexis.toml");

		// 2055 entries
		ScriptsConfig sConfig = config.getScriptsConfig();
		scripts = new SheetsLoader(config.getApplicationName(), sConfig.getId(), sConfig.getRange()).load();

		// 3232 entries
//		scripts = new CsvLoader("/scripts.csv").load();

		initJDA();
		initCommandler();

		DebugConfig debug = config.getDebugConfig();

		if (debug.isDatabaseEnabled())
			dbManager = new DatabaseManager(config.getDatabaseConfig());
	}

	private static void initCommandler() throws IOException, GeneralSecurityException {
		commandler = new JDACommandler(jda, new AlexisConfiler("<"));

		commandler.registerParser(new LanguageParser(), Language.class);

		commandler.registerValidator(Database.class, new DatabaseValidator());
		commandler.registerValidator(Achievements.class, new AchievementsValidator());
		commandler.registerValidator(Supported.class, new SupportedValidator());
		commandler.registerValidator(Elevated.class, new ElevatedValidator(127578559790186497L));

		YouTubeHelper youtube = new YouTubeHelper("Alexis");

		commandler.registerBuilder(new YouTubeSearchResultBuilder(youtube), SearchResult.class);
		commandler.registerBuilder(new UrbanDefinitionBuilder(), UrbanDefinition.class);
		commandler.registerBuilder(new OsuPlayerBuilder(), OsuPlayer.class);
		commandler.registerBuilder(new SteamGameBuilder(), SteamGame.class);
		commandler.registerBuilder(new NanoUserBuilder(), NanoUser.class);

		commandler.registerModules(
			new HelpModule(),
//			new AmazonHandler(),
			new BotHandler(),
			new BrainfuckHandler(),
			new CleverbotHandler(),
			new DevHandler(),
			new EmoteHandler(),
			new GreetingModule(),
			new GuildHandler(),
			new MiscHandler(),
			new LanguageHandler(),
			new MusicHandler(youtube),
			new NanowrimoHandler(),
			new OsuHandler(),
			new PrefixHandler(),
			new RolesHandler(),
			new RuneScapeHandler(),
			new SkillsHandler(),
			new SteamHandler(),
			new TranslateHandler(),
			new TwitchHandler(),
			new UrbanDictionaryHandler(),
			new UserHandler(),
			new VoiceHandler(),
			new YouTubeHandler(youtube)
		);
	}

	private static void initJDA() throws LoginException {
		DiscordConfig discord = config.getDiscordConfig();

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(discord.getToken());
		builder.setCorePoolSize(4);
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.addEventListener(new EventHandler());
		builder.setGame(Game.of(Game.GameType.WATCHING, "Seth turn me on!"));

		jda = builder.build();
	}

	public static DatabaseManager getDatabaseManager() {
		return dbManager;
	}
}
