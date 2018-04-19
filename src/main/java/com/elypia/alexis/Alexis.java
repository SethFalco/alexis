package com.elypia.alexis;

import com.elypia.alexis.discord.*;
import com.elypia.alexis.utils.*;
import com.mongodb.MongoClient;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Alexis {

	public static final long START_TIME = System.currentTimeMillis();

	public static JSONObject config;
	public static MongoClient client;

	public static void main(String[] args) {
	    // Config
		String configPath = "./config.json";

		if (args.length > 0)
			configPath = args[0];

		Path path = Paths.get(configPath);

        try {
            String string = Files.lines(path).collect(Collectors.joining());
            config = new JSONObject(string);
        } catch (IOException ex) {
            ExitCode code = ExitCode.FAILED_TO_READ_CONFIG;
            BotUtils.LOGGER.log(Level.SEVERE, code.getMessage(), ex);
            System.exit(code.getStatusCode());
        }

		// Database
        JSONObject database = config.getJSONObject("database");
        client = new MongoClient(database.getString("ip"), database.getInt("port"));

		// JDA
		try {
			Chatbot bot = new Chatbot();
		} catch (LoginException ex) {
			ExitCode code = ExitCode.FAILED_TO_INIT_BOT;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage(), ex);
			System.exit(code.getStatusCode());
		}
	}
}
