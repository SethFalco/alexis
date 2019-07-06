package com.elypia.alexis.utils;

import com.elypia.commandler.CommandlerEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import org.slf4j.*;

import java.util.*;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=";

	private static final Logger logger = LoggerFactory.getLogger(BotUtils.class);

	private BotUtils() {
		// Don't construct this
	}

	public static EmbedBuilder newEmbed(CommandlerEvent<?> event) {
		return newEmbed(((GenericGuildEvent)event.getSource()).getGuild());
	}

	public static EmbedBuilder newEmbed(Guild guild) {
		EmbedBuilder builder = new EmbedBuilder();

		if (guild != null)
			builder.setColor(guild.getSelfMember().getColor());

		return builder;
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

	public static String getInviteUrl(User user) {
		Objects.requireNonNull(user);

		if (!user.isBot())
			throw new RuntimeException("User is not a bot.");

		return BOT_URL + user.getId();
	}
}
