package com.elypia.alexis;

import com.elypia.alexis.audio.controllers.LocalAudioController;
import com.elypia.alexis.commandler.annotations.validation.command.*;
import com.elypia.alexis.commandler.validators.command.*;
import com.elypia.alexis.handlers.EventHandler;
import com.elypia.alexis.handlers.modules.*;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.pages.PageBuilder;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.json.*;
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
		if (!doc) {
			startUpTime = System.currentTimeMillis();

			initDatabase();
			initJDA();
		}

		initCommandler();

		if (doc) {
			PageBuilder builder = new PageBuilder(commandler);
			builder.setName("Alexis");
			builder.setAvatar("./resources/alexis.png");
			builder.setFavicon("./resources/favicon.ico");
			builder.setDescription("Alexis is a multi purpose bot for Discord!");

			builder.build(new File("." + File.separator + "pages" + File.separator));
		}
	}

	private void initDatabase() {
		JSONObject db = Config.getConfig("database");

		if (db.getBoolean("enabled")) {
			client = new MongoClient(db.getString("ip"), db.getInt("port"));

			morphia = new Morphia();
			morphia.mapPackage("com.elypia.alexis.entities");

			store = morphia.createDatastore(client, "alexis");
			store.ensureIndexes();
		}
	}

	private void initJDA() throws LoginException {
		statuses = new ArrayList<>();

		JSONObject discord = Config.getConfig("discord");

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(discord.getString("token"));
		builder.setCorePoolSize(10);
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.addEventListener(new EventHandler(this));

		JSONArray statusesArray = discord.getJSONArray("statuses");

		for (int i = 0; i < statusesArray.length(); i++) {
			JSONObject status = statusesArray.getJSONObject(i);
			int key = status.getInt("key");
			String text = status.getString("text");

			statuses.add(Game.of(Game.GameType.fromKey(key), text));
		}

		builder.setGame(statuses.get(0));

		jda = builder.buildAsync();
	}

	private void initCommandler() {
		JSONObject api = Config.getConfig("api_keys");
		commandler = new Commandler(jda, new AlexisConfiler());

		commandler.registerModules(
			new AmazonHandler(api.getJSONObject("amazon")),
			new BotHandler(),
			new BrainfuckHandler(),
			new CleverbotHandler(api.getString("cleverbot")),
			new DevHandler(),
			new EmoteHandler(),
			new GreetingModule(),
			new GuildHandler(),
			new UtilHandler(),
			new MusicHandler(LocalAudioController.class),
			new MyAnimeListHandler(api.getString("mal")),
			new NanowrimoHandler(),
			new OsuHandler(api.getString("osu")),
			new PrefixHandler(),
			new RuneScapeHandler(),
			new SteamHandler(api.getString("steam")),
			new TwitchHandler(api.getString("twitch")),
			new UrbanDictionaryHandler(),
			new UserHandler(),
			new VoiceHandler(),
			new YouTubeHandler(api.getString("google"))
		);

		commandler.registerValidator(Database.class, new DatabaseValidator());
		commandler.registerValidator(Developer.class, new DeveloperValidator());
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
