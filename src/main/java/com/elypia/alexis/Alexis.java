package com.elypia.alexis;

import com.elypia.alexis.discord.*;
import com.elypia.alexis.utils.*;
import com.mongodb.MongoClient;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;

public class Alexis {

	public static final long START_TIME = System.currentTimeMillis();

	public static MongoClient client;

	public static void main(String[] args) {
		String configPath = "./settings.json";

		if (args.length > 0)
			configPath = args[0];

		Config.getConfiguration(configPath);

		// Database
        client = new MongoClient(Config.ip, Config.port);

		// JDA
		try {
			Chatbot bot = new Chatbot(client);
		} catch (LoginException ex) {
			ExitCode code = ExitCode.FAILED_TO_INIT_BOT;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage(), ex);
			System.exit(code.getStatusCode());
		}
	}
}
