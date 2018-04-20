package com.elypia.alexis;

import com.elypia.alexis.discord.*;
import com.elypia.alexis.utils.*;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.logging.impl.Log4JLogger;
import org.json.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Alexis {

	public static final long START_TIME = System.currentTimeMillis();

	public static Chatbot bot;
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

        if (database.getBoolean("enabled"))
            client = new MongoClient(database.getString("ip"), database.getInt("port"));

		// JDA
		try {
			bot = new Chatbot();
		} catch (LoginException ex) {
			ExitCode code = ExitCode.FAILED_TO_INIT_BOT;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage(), ex);
			System.exit(code.getStatusCode());
		}
	}

    /**
     * @return The lead developer from the list of developers specified
     *         in the config file. The lead developer is assumed to be at
     *         point [0] of the developers array.
     */

	public static User getDev() {
		JSONObject devs = config.getJSONObject("discord");
		JSONArray array = devs.getJSONArray("developers");
		JSONObject dev = array.getJSONObject(0);

		return bot.getJDA().getUserById(dev.getLong("id"));
	}
}
