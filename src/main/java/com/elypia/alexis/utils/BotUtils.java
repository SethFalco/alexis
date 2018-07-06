package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.AlexisConfig;
import com.elypia.alexis.entities.UserData;
import com.elypia.commandler.events.AbstractEvent;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GenericGuildMemberEvent;

import java.util.Collection;
import java.util.logging.*;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";
	private static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	private BotUtils() {
		// Don't construct this
	}

	public static EmbedBuilder createEmbedBuilder(AbstractEvent event) {
		return createEmbedBuilder(event.getMessage().getGuild());
	}

	public static EmbedBuilder createEmbedBuilder(Guild guild) {
		EmbedBuilder builder = new EmbedBuilder();

		if (guild != null)
			builder.setColor(guild.getSelfMember().getColor());

		return builder;
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
		AlexisConfig config = Alexis.getConfig();
		boolean databaseEnabled = config.getDatabaseConfig().isEnabled();
		long dev = config.getDiscordConfig().getAuthors().get(0).getId();

		try {
			UserData data = UserData.query(dev);
			return databaseEnabled && data != null;
		} catch (NullPointerException ex) {
			return false;
		}
	}

	public static void sendHttpError(AbstractEvent event, Throwable failure) {
		String message = "Sorry, the command failed. I'm reporting this to Seth, perhaps trying again later?";
		event.getMessageEvent().getChannel().sendMessage(message).queue();

		log(Level.SEVERE, failure.getMessage(), failure);
	}

	public static String formInviteUrl(User user) {
		return String.format(BOT_URL, user.getIdLong());
	}

	/**
	 * Call the static
	 *
	 * @param level
	 * @param message
	 * @param args
	 */

	// Add exception for 7777
	public static void log(Level level, String message, Object... args) {
	    message = String.format(message, args);
		LOGGER.log(level, message);

		if (level == Level.INFO || level == Level.WARNING || level == Level.SEVERE) {
			String color = (level == Level.INFO) ? "+" : "-";

			message = "```diff\n" + level.getName() + ":\n" + message + "```";
			message = message.replace("\n", "\n" + color + " ");

			long channelId = Alexis.getConfig().getDiscordConfig().getLogChannel();
			MessageChannel channel = Alexis.getChatbot().getJDA().getTextChannelById(channelId);

			if (level == Level.SEVERE) {
				long userId = Alexis.getConfig().getDiscordConfig().getAuthors().get(0).getId();
				User user = Alexis.getChatbot().getJDA().getUserById(userId);
				message = "_ _\n" + user.getAsMention() + "\n\n" + message;
			}

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
