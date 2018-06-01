package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";
	private static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	private BotUtils() {

	}

	public static void sendHttpError(MessageEvent event, IOException failure) {
		String message = "Sorry, the command failed. I'm reporting this to Seth, perhaps trying again later?";
		event.getMessageEvent().getChannel().sendMessage(message).queue();

		log(Level.SEVERE, failure.getMessage(), failure);
	}

	public static String formInviteUrl(User user) {
		return String.format(BOT_URL, user.getIdLong());
	}

	public static void log(Level level, String message, Object... args) {
	    String msg = String.format(message, args);
		LOGGER.log(level, msg, args);

		String color = "+";

		if (level == Level.SEVERE || level == Level.WARNING)
			color = "-";

		msg = "```diff\n" + msg + "```";
		msg = msg.replace("\n", "\n" + color + " ");

		long id = Config.getConfig("discord").getLong("log_channel");
		MessageChannel channel = Alexis.getChatbot().getJDA().getTextChannelById(id);
		channel.sendMessage(msg).queue();
	}

	public static void logBotInfo() {
		JDA jda = Alexis.getChatbot().getJDA();

		int guilds = jda.getGuilds().size();
		int users = jda.getUsers().size();
		String format = "I'm in %,d guilds and can currently see %,d users!";

		log(Level.INFO, format, guilds, users);
	}
}
