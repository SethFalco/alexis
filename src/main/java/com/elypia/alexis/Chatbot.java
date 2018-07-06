package com.elypia.alexis;

import com.elypia.alexis.commandler.*;
import com.elypia.alexis.commandler.builders.*;
import com.elypia.alexis.config.*;
import com.elypia.alexis.handlers.EventHandler;
import com.elypia.alexis.handlers.modules.*;
import com.elypia.alexis.utils.AlexisConfiler;
import com.elypia.alexis.youtube.YouTubeHelper;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.pages.PageBuilder;
import com.elypia.elypiai.osu.OsuPlayer;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.mongodb.morphia.*;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;

public class Chatbot {

	private long startUpTime;

	private JDA jda;
	private List<Game> statuses;

	private Commandler commandler;

	private MongoClient client;
	private Morphia morphia;
	private Datastore store;

	public Chatbot(boolean doc) throws LoginException, IOException {
		initCommandler();

		if (doc) {
			PageBuilder builder = new PageBuilder(commandler);
			builder.setName("Alexis");
			builder.setAvatar("./resources/alexis.png");
			builder.setFavicon("./resources/favicon.ico");
			builder.setDescription("Alexis is a multi purpose bot for Discord!");

			builder.build("." + File.separator + "pages" + File.separator);
			return;
		}

		startUpTime = System.currentTimeMillis();
		initDatabase();
		initJDA();

		commandler.setJDA(jda);
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
		commandler = new Commandler(new AlexisConfiler("<"));

		commandler.registerValidator(Database.class, new DatabaseValidator());

		YouTubeHelper youtube = new YouTubeHelper(api.getGoogle(), "Alexis");

		commandler.registerSender(SearchResult.class, new YouTubeSearchResultBuilder(youtube));
		commandler.registerSender(UrbanDefinition.class, new UrbanDefinitionBuilder());
		commandler.registerSender(OsuPlayer.class, new OsuPlayerBuilder());
		commandler.registerSender(SteamGame.class, new SteamGameBuilder());



		commandler.registerModules(
//			new AmazonHandler(api.getAmazonDetails()),
			new BotHandler(),
			new BrainfuckHandler(),
//			new CleverbotHandler(api.getCleverbot()),
			new DevHandler(),
			new EmoteHandler(),
			new GreetingModule(),
			new GuildHandler(),
			new UtilHandler(),
			new MusicHandler(youtube),
//			new MyAnimeListHandler(api.getMyAnimeList()),
//			new NanowrimoHandler(),
			new OsuHandler(api.getOsu()),
			new PrefixHandler(),
			new RuneScapeHandler(),
//			new SteamHandler(api.getSteam()),
//			new TwitchHandler(api.getTwitch()),
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
