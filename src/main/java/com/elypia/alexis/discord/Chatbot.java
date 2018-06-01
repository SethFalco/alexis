package com.elypia.alexis.discord;

import com.elypia.alexis.discord.audio.controllers.LocalAudioController;
import com.elypia.alexis.EventHandler;
import com.elypia.alexis.discord.modules.*;
import com.elypia.alexis.utils.AlexisConfiler;
import com.elypia.alexis.utils.Config;
import com.elypia.commandler.Commandler;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Chatbot {

	private long startUpTime;

	private JDA jda;
	private List<Game> statuses;

	private MongoClient client;
	private Morphia morphia;
	private Datastore store;

	public Chatbot() throws LoginException, InterruptedException {
		startUpTime = System.currentTimeMillis();

	    initDatabase();
		initJDA();
		initCommandler();
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

	private void initJDA() throws LoginException, InterruptedException {
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
		Commandler commandler = new Commandler(jda, new AlexisConfiler());

		commandler.registerModules(
			new AmazonHandler(api.getJSONObject("amazon")),
			new BotHandler(),
			new BrainfuckHandler(),
			new CleverbotHandler(api.getString("cleverbot")),
			new DevHandler(),
			new EmoteHandler(),
			new GuildHandler(),
			new UtilHandler(),
			new MusicHandler(LocalAudioController.class),
			new MyAnimeListHandler(api.getString("mal")),
			new NanowrimoHandler(),
			new OsuHandler(api.getString("osu")),
			new RuneScapeHandler(),
			new SteamHandler(api.getString("steam")),
			new TwitchHandler(api.getString("twitch")),
			new UrbanDictionaryHandler(),
			new UserHandler(),
			new VoiceHandler(),
			new YouTubeHandler(api.getString("google"))
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
