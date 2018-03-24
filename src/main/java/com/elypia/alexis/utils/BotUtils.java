package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.events.MessageEvent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BotUtils {

	private BotUtils() {

	}

	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	public static void httpFailure(MessageEvent event, IOException failure) {
		// Log the exception to console, was likely time out or API in use is deprecated.
		LOGGER.log(Level.SEVERE, "Unirest request failed.", failure);

		// Let the user know what happened and apologise.
		String message = "Sorry! I'm don't know why the command failed but I'm reporting this to Seth, perhaps trying again later?";
		event.getChannel().sendMessage(message).queue();
	}
}
