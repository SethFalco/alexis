package com.elypia.alexis;

import com.elypia.alexis.commandler.builders.*;
import com.elypia.alexis.commandler.parsers.LanguageParser;
import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.config.BotConfig;
import com.elypia.alexis.config.embedded.DiscordConfig;
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
import com.elypia.elyscript.ScriptStore;
import com.elypia.elyscript.sheets.SheetsLoader;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
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
        // Alexis Configuration
        config = BotConfig.load("./alexis.toml");

        // Alexis Scripts
        scripts = new SheetsLoader(
            config.getApplicationName(),
            config.getScriptsConfig().getId(),
            config.getScriptsConfig().getRange()
        ).load();

		initJDA();
		initCommandler();

		if (config.getDebugConfig().isDatabaseEnabled())
			dbManager = new DatabaseManager(config.getDatabaseConfig());
	}

	private static void initCommandler() throws IOException, GeneralSecurityException {
		commandler = new JDACommandler(jda, new AlexisConfiler("<"));
		commandler.init("com.elypia.alexis.commandler");


		commandler.addParser(new LanguageParser(), Language.class);

		commandler.addValidator(Database.class, new DatabaseValidator());
		commandler.addValidator(Achievements.class, new AchievementsValidator());
		commandler.addValidator(Supported.class, new SupportedValidator());
		commandler.addValidator(Elevated.class, new ElevatedValidator(127578559790186497L));

		YouTubeHelper youtube = new YouTubeHelper("Alexis");

		commandler.addBuilder(new YouTubeSearchResultBuilder(youtube), SearchResult.class);
		commandler.addBuilder(new UrbanDefinitionBuilder(), UrbanDefinition.class);
		commandler.addBuilder(new OsuPlayerBuilder(), OsuPlayer.class);
		commandler.addBuilder(new SteamGameBuilder(), SteamGame.class);
		commandler.addBuilder(new NanoUserBuilder(), NanoUser.class);

		commandler.addModules(
			new HelpModule(),
			new AmazonHandler(),
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

		commandler.registerModules("com.elypia.alexis.handlers.modules");
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
