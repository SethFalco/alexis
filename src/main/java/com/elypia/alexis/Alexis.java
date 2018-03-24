package com.elypia.alexis;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.ChatbotConfig;
import com.elypia.alexis.discord.audio.controllers.LocalAudioController;
import com.elypia.alexis.discord.handlers.commands.modules.*;
import com.elypia.alexis.discord.managers.CommandManager;
import com.elypia.alexis.discord.managers.EventManager;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.ExitCode;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;

public class Alexis {

	public static final long START_TIME = System.currentTimeMillis();

	public static void main(String[] args) {
		String configPath = "./settings.json";

		if (args.length > 0)
			configPath = args[0];

		ChatbotConfig config = ChatbotConfig.getConfiguration(configPath);

		if (config == null) {
			ExitCode code = ExitCode.UNKNOWN_CONFIG_ERROR;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage());
			System.exit(code.getStatusCode());
		}

		MongoClient client = new MongoClient(config.getIp(), config.getPort());
		MongoDatabase global = client.getDatabase("global");
		MongoCollection<Document> api = global.getCollection("api_details");

		try {
			Chatbot bot = new Chatbot(config, client);

			EventManager events = new EventManager(bot);
			CommandManager commands = new CommandManager(bot);

			commands.registerModules(
				new AmazonHandler(api, AmazonEndpoint.US),
				new BotHandler(),
				new BrainfuckHandler(),
				new CleverbotHandler(client),
				new MathHandler(),
				new MusicHandler(LocalAudioController.class),
				new NanowrimoHandler(client.getDatabase("users")),
				new RuneScapeHandler(),
				new SteamHandler(api),
				new UrbanDictionaryHandler(),
				new UserHandler()
			);

			bot.registerManagers(events, commands);
		} catch (LoginException ex) {
			ExitCode code = ExitCode.FAILED_TO_INIT_BOT;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage(), ex);
			System.exit(code.getStatusCode());
		}
	}
}
