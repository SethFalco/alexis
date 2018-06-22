package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.UserData;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GenericGuildMemberEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.*;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";
	private static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	private BotUtils() {

	}

	public static String buildCustomMessage(GenericGuildMemberEvent event, String message) {
		Guild guild = event.getGuild();
		Member member = event.getMember();

		message = message.replace("{guild.name}", guild.getName());

		message = message.replace("{user.name}", member.getEffectiveName());
		message = message.replace("{user.mention}", member.getAsMention());

		return message;
	}

	public static TextChannel getWriteableChannel(GenericGuildEvent event) {
		Guild guild = event.getGuild();
		Collection<TextChannel> channels = guild.getTextChannelsByName("general", true);

		for (TextChannel channel : channels) {
			if (channel.canTalk())
				return channel;
		}

		channels = guild.getTextChannels();

		for (TextChannel channel : channels) {
			if (channel.canTalk())
				return channel;
		}

		return null;
	}

	public static boolean isDatabaseAlive() {
		boolean config = Config.getConfig("database").getBoolean("enabled");
		long dev = Config.getConfig("discord").getJSONArray("authors").getJSONObject(0).getLong("id");

		try {
			UserData data = UserData.query(dev);
			return config && data != null;
		} catch (NullPointerException ex) {
			return false;
		}
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
	    message = String.format(message, args);
		LOGGER.log(level, message);

		String color = (level == Level.SEVERE || level == Level.WARNING) ? "-" : "+";

		message = "```diff\n" + level.getName() + ": " + message + "```";
		message = message.replace("\n", "\n" + color + " ");

		JSONObject discord = Config.getConfig("discord");
		long channelId = discord.getLong("log_channel");
		MessageChannel channel = Alexis.getChatbot().getJDA().getTextChannelById(channelId);

		if (level == Level.SEVERE) {
			long userId = discord.getJSONArray("authors").getJSONObject(0).getLong("id");
			User user = Alexis.getChatbot().getJDA().getUserById(userId);
			channel.sendMessage(user.getAsMention() + "\n" + message).queue();
		} else {
			channel.sendMessage(message).queue();
		}
	}

	public static void logBotInfo() {
		JDA jda = Alexis.getChatbot().getJDA();

		int guilds = jda.getGuilds().size();
		int users = jda.getUsers().size();
		String format = "I'm in %,d guilds and can currently see %,d users!";

		log(Level.INFO, format, guilds, users);
	}
}
