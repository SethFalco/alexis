package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.entities.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";

	private BotUtils() {

	}

	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	public static void httpFailure(MessageEvent event, IOException failure) {
		// Log the exception to console, was likely time out or API in use is deprecated.
		LOGGER.log(Level.SEVERE, "Unirest request failed.", failure);

		// Let the user know what happened and apologise.
		String message = "Sorry! I'm don't know why the command failed but I'm reporting this to Seth, perhaps trying again later?";
		event.getMessageEvent().getChannel().sendMessage(message).queue();
	}

	public static String inviteUrl(User user) {
		return String.format(BOT_URL, user.getIdLong());
	}

	public static void log(Level level, String message, String... args) {
		LOGGER.log(level, message, args);
	}

	public static <T extends Throwable> void log(Level level, String message, T ex) {
		LOGGER.log(level, message, ex);
	}
}
