package com.elypia.alexis;

import com.elypia.alexis.config.AlexisConfig;
import com.elypia.alexis.utils.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Level;

import static com.elypia.alexis.utils.ExitCode.*;

public class Alexis {

	private static Chatbot bot;
	private static AlexisConfig alexisConfig;

	public static void main(String[] args) {
		try {
			alexisConfig = AlexisConfig.initConfig("./config.json");
			bot = new Chatbot(args.length > 0 && args[0].equalsIgnoreCase("-doc"));
		} catch (LoginException | IOException ex) {
			ExitCode code = ex instanceof IOException ? FAILED_TO_READ_CONFIG : FAILED_TO_INIT_BOT;
			BotUtils.log(Level.SEVERE, code.getMessage());
			System.exit(code.getStatusCode());
		}
	}

	public static Chatbot getChatbot() {
		return bot;
	}

	public static AlexisConfig getConfig() {
		return alexisConfig;
	}
}
