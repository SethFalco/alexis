package com.elypia.alexis;

import java.util.logging.*;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.commands.modules.*;

public class Alexis {
	
	public static final long START_TIME = System.currentTimeMillis();
	
	public static void main(String[] args) {
		String token = "***REMOVED***";
		
		try {
			Chatbot bot = new Chatbot(token);
			
			bot.registerModules(
				new BotHandler(),
				new NaNoWriMoHandler(),
				new RuneScapeHandler(),
				new RuneScapeNotifyHandler(),
				new UrbanDictionaryHandler(),
				new UserHandler()
			);
		} catch (Exception ex) {
			AlexisUtils.LOGGER.log(Level.SEVERE, "Alexis was unable to start up!", ex);
			System.exit(0);
		}
	}
}
