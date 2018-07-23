package com.elypia.alexis;

import com.elypia.alexis.commandler.builders.*;
import com.elypia.alexis.commandler.parsers.LanguageParser;
import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.config.*;
import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.handlers.EventHandler;
import com.elypia.alexis.handlers.modules.*;
import com.elypia.alexis.utils.AlexisConfiler;
import com.elypia.commandler.JDACommandler;
import com.elypia.elypiai.nanowrimo.NanoUser;
import com.elypia.elypiai.osu.OsuPlayer;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.utils.Language;
import com.elypia.elyscript.ElyScriptStore;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.apache.commons.csv.*;
import org.mongodb.morphia.*;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;

public class Chatbot {

	private static final String SCRIPT_LOCATION = "/scripts.csv";

	private long startUpTime;

	private JDA jda;
	private List<Game> statuses;

	private JDACommandler commandler;

	private ElyScriptStore scriptStore;
	private List<Language> supportedLanguages;

	private MongoClient client;
	private Morphia morphia;
	private Datastore store;

	public Chatbot(boolean doc) throws LoginException, IOException {
		initJDA();
		initDatabase();
		initCommandler();
		initScriptStore();

//		if (doc) {
//			PageBuilder builder = new PageBuilder(commandler);
//			builder.setName("Alexis");
//			builder.setAvatar("./resources/alexis.png");
//			builder.setFavicon("./resources/favicon.ico");
//			builder.setDescription("Alexis is a multi purpose bot for Discord!");
//
//			builder.build("." + File.separator + "pages" + File.separator);
//			return;
//		}

		startUpTime = System.currentTimeMillis();

	}

	private void initScriptStore() throws IOException {
		scriptStore = new ElyScriptStore();
		supportedLanguages = new ArrayList<>();

		try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(SCRIPT_LOCATION))) {
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			List<CSVRecord> records = csvParser.getRecords();
			CSVRecord head = records.get(0);

			for (int i = 1; i < head.size(); i++) {
				String content = head.get(i);

				if (content.isEmpty())
					throw new RuntimeException("Unnamed language found in language file.");

				Language language = Language.getByCode(content);

				if (language == null)
					throw new RuntimeException("Unknown language found in language file.");

				supportedLanguages.add(language);
			}

			for (int i = 1; i < records.size(); i++) {
				CSVRecord record = records.get(i);
				String key = record.get(0);

				if (key == null || key.isEmpty())
					continue;

				String defaultContent = record.get(1);

				if (defaultContent == null || defaultContent.isEmpty())
					throw new RuntimeException("Default language must be populated.");

				for (int ii = 1; ii < record.size(); ii++) {
					String content = record.get(ii);

					if (content == null || content.isEmpty())
						content = defaultContent;

					Language language = supportedLanguages.get(ii - 1);

					scriptStore.store(ElyScriptStore.combineKeys(key, language.getCode()), content);
				}
			}
		}
	}

	private void initDatabase() {
		DatabaseConfig config = Alexis.getConfig().getDatabaseConfig();

		if (config.isEnabled()) {
			client = new MongoClient(config.getIp(), config.getPort());

			morphia = new Morphia();
			morphia.mapPackage("com.elypia.alexis.entities");

			store = morphia.createDatastore(client, "alexis");
			store.ensureIndexes();
		}
	}

	private void initJDA() throws LoginException {
		statuses = new ArrayList<>();

		DiscordConfig config = Alexis.getConfig().getDiscordConfig();

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(config.getToken());
		builder.setCorePoolSize(10);
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.addEventListener(new EventHandler(this));

		List<Game> games = config.getStatuses();
		statuses.addAll(games);

		if (!statuses.isEmpty())
			builder.setGame(statuses.get(0));

		jda = builder.buildAsync();
	}

	private void initCommandler() {
		ApiKeys api = Alexis.getConfig().getApiKeys();
		commandler = new JDACommandler(jda, new AlexisConfiler("<"));

		commandler.registerParser(new LanguageParser(), Language.class);

		commandler.registerValidator(Database.class, new DatabaseValidator());
		commandler.registerValidator(Achievements.class, new AchievementsValidator());
		commandler.registerValidator(Supported.class, new SupportedValidator());

		YouTubeHelper youtube = new YouTubeHelper(api.getGoogle(), "Alexis");

		commandler.registerBuilder(new YouTubeSearchResultBuilder(youtube), SearchResult.class);
		commandler.registerBuilder(new UrbanDefinitionBuilder(), UrbanDefinition.class);
		commandler.registerBuilder(new OsuPlayerBuilder(), OsuPlayer.class);
		commandler.registerBuilder(new SteamGameBuilder(), SteamGame.class);
		commandler.registerBuilder(new NanoUserBuilder(), NanoUser.class);

		commandler.registerModules(
			new AmazonHandler(api.getAmazonDetails()),
			new BotHandler(),
			new BrainfuckHandler(),
			new CleverbotHandler(api.getCleverbot()),
			new DevHandler(),
			new EmoteHandler(),
			new GreetingModule(),
			new GuildHandler(),
			new UtilHandler(),
			new LanguageHandler(),
			new MusicHandler(youtube),
			new NanowrimoHandler(),
			new OsuHandler(api.getOsu()),
			new PrefixHandler(),
			new RuneScapeHandler(),
			new SteamHandler(api.getSteam()),
			new TwitchHandler(api.getTwitch()),
			new UrbanDictionaryHandler(),
			new UserHandler(),
			new VoiceHandler(),
			new YouTubeHandler(youtube)
		);
	}

	public long getStartUpTime() {
		return startUpTime;
	}

	public JDA getJDA() {
		return jda;
	}

	public List<Game> getStatuses() {
		return statuses;
	}

	public ElyScriptStore getScriptStore() {
		return scriptStore;
	}

	public List<Language> getSupportedLanguages() {
		return supportedLanguages;
	}

	public MongoClient getDatabase() {
		return client;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public Datastore getDatastore() {
		return store;
	}
}
