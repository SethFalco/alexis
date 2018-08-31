package com.elypia.alexis;

import com.elypia.alexis.commandler.builders.*;
import com.elypia.alexis.commandler.parsers.LanguageParser;
import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.config.*;
import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.handlers.EventHandler;
import com.elypia.alexis.handlers.modules.*;
import com.elypia.alexis.utils.AlexisConfiler;
import com.elypia.commandler.jda.JDACommandler;
import com.elypia.elypiai.nanowrimo.NanoUser;
import com.elypia.elypiai.osu.OsuPlayer;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.utils.Language;
import com.elypia.elyscript.ElyScriptStore;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.apache.commons.csv.*;
import org.mongodb.morphia.*;
import org.slf4j.*;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;

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

	/**
	 * The path where the configuration file is held, the application should be launched
	 * from the same directory as runnable and config.
	 */
	private static final String CONFIG_PATH = "./config.json";

	/**
	 * The path where script / language files are held, this contains all {@link String}s
	 * this application requires and is also used for internationalization.
	 */
	private static final String SCRIPT_PATH = "/scripts.csv";

	/**
	 * Creates an instance of the bot configuration object and loads the configuration
	 * from the {@link #CONFIG_PATH} defined.
	 */
	public static BotConfiguration config = BotConfiguration.instance(CONFIG_PATH);

	/**
	 * The JDA instance used to communicate with Discord.
	 */
	public static JDA jda;

	/**
	 * A list of Discord statues which is we loop through as a way to give
	 * annoucements or significant notices.
	 */
	public static List<Game> statuses;

	/**
	 * The Commandler instance used to manage most of the command handling.
	 */
	public static JDACommandler commandler;

	/**
	 * The ElyScript store to validate ElyScript and store scripts in order to
	 * query as required.
	 */
	public static ElyScriptStore scriptStore;

	/**
	 * A list of all of Alexis' supported languages, this is determined by looking
	 * at the top row of the file at {@link #SCRIPT_PATH} and seeing which languages
	 * are specified.
	 */
	public static List<Language> supportedLanguages;

	/**
	 * The connection to the MongoDB database which stores everything Alexis
	 * requires.
	 */
	public static MongoClient client;

	/**
	 * Morphia is a library which allows us to map objects from the database to
	 * Java objects one to one, this is ues to make it cleaner to query and update the database.
	 */
	public static Morphia morphia;

	/**
	 * A database store is used to perform database queries and updates.
	 */
	public static Datastore store;

	public static void main(String[] args) throws IOException, LoginException {
		initElyScript();
		initCommandler();
		initDatabase();
		initJDA();
	}

	private static void initElyScript() throws IOException {
		scriptStore = new ElyScriptStore();
		supportedLanguages = new ArrayList<>();

		try (InputStreamReader reader = new InputStreamReader(Alexis.class.getResourceAsStream(SCRIPT_PATH))) {
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			List<CSVRecord> records = csvParser.getRecords();
			CSVRecord head = records.get(0);

			for (int i = 1; i < head.size(); i++) {
				String content = head.get(i);

				if (content.isEmpty()) {
					logger.warn("Unnamed script head is being ignored.");
					continue;
				}

				Language language = Language.getByCode(content);

				if (language == null)
					throw new RuntimeException("Unknown language found in language file.");

				supportedLanguages.add(language);
			}

			for (int i = 1; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				String key = record.get(0);

				if (key.isEmpty() || key.startsWith("//"))
					continue;

				String defaultContent = record.get(1);

				if (defaultContent.isEmpty())
					throw new RuntimeException("Default language must be populated.");

				for (int ii = 1; ii < record.size(); ii++) {
					String content = record.get(ii);

					if (content.isEmpty())
						content = defaultContent;

					Language language = supportedLanguages.get(ii - 1);

					scriptStore.store(content, key, language.getCode());
				}
			}
		}
	}

	private static void initCommandler() {
		commandler = new JDACommandler(jda, new AlexisConfiler("<"));

		commandler.registerParser(new LanguageParser(), Language.class);

		commandler.registerValidator(Database.class, new DatabaseValidator());
		commandler.registerValidator(Achievements.class, new AchievementsValidator());
		commandler.registerValidator(Supported.class, new SupportedValidator());

		YouTubeHelper youtube = new YouTubeHelper(config.getApiKeys().getGoogle(), "Alexis");

		commandler.registerBuilder(new YouTubeSearchResultBuilder(youtube), SearchResult.class);
		commandler.registerBuilder(new UrbanDefinitionBuilder(), UrbanDefinition.class);
		commandler.registerBuilder(new OsuPlayerBuilder(), OsuPlayer.class);
		commandler.registerBuilder(new SteamGameBuilder(), SteamGame.class);
		commandler.registerBuilder(new NanoUserBuilder(), NanoUser.class);

		commandler.registerModules(
			new HelpModule(),
			new AmazonHandler(),
			new BotHandler(),
			new BrainfuckHandler(),
			new CleverbotHandler(),
			new DevHandler(),
			new EmoteHandler(),
			new GreetingModule(),
			new GuildHandler(),
			new UtilHandler(),
			new LanguageHandler(),
			new MusicHandler(youtube),
			new NanowrimoHandler(),
			new OsuHandler(),
			new PrefixHandler(),
			new RuneScapeHandler(),
			new SteamHandler(),
			new TwitchHandler(),
			new UrbanDictionaryHandler(),
			new UserHandler(),
			new VoiceHandler(),
			new YouTubeHandler(youtube)
		);
	}

	private static void initDatabase() {
		DatabaseConfig database = config.getDatabaseConfig();

		if (database.isEnabled()) {
			String ip = database.getIp();
			int port = database.getPort();
			ServerAddress address = new ServerAddress(ip, port);

			String user = database.getUser();
			String source = database.getDatabase();
			char[] password = database.getPassword().toCharArray();
			MongoCredential credentials = MongoCredential.createCredential(user, source, password);

			var settings = MongoClientSettings.builder().applyToClusterSettings(builder -> {
				builder.hosts(List.of(address));
			}).credential(credentials).build();

			client = MongoClients.create(settings);

			morphia = new Morphia();
			morphia.mapPackage("com.elypia.alexis.entities");

			store = morphia.createDatastore((com.mongodb.MongoClient)client, "alexis");
			store.ensureIndexes();
		}
	}

	private static void initJDA() throws LoginException {
		DiscordConfig discord = config.getDiscordConfig();

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(discord.getToken());
		builder.setCorePoolSize(4);
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.addEventListener(new EventHandler());

		statuses = discord.getStatuses();

		if (!statuses.isEmpty())
			builder.setGame(statuses.get(0));

		jda = builder.build();
	}
}
