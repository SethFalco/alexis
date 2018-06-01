package com.elypia.alexis;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.Config;
import com.elypia.alexis.utils.ExitCode;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Level;

import static com.elypia.alexis.utils.ExitCode.FAILED_TO_INIT_BOT;
import static com.elypia.alexis.utils.ExitCode.FAILED_TO_READ_CONFIG;

public class Alexis {

	private static Chatbot bot;

	public static void main(String[] args) {
		try {
			Config.init("config.json");
			bot = new Chatbot();
		} catch (LoginException | InterruptedException| IOException ex) {
			ExitCode code = ex instanceof IOException ? FAILED_TO_READ_CONFIG : FAILED_TO_INIT_BOT;
			BotUtils.log(Level.SEVERE, code.getMessage());
			System.exit(code.getStatusCode());
		}
	}

	public static Chatbot getChatbot() {
		return bot;
	}
}
