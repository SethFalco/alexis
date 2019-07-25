/*
 * Copyright (C) 2019  Elypia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.elypia.alexis.utils;

import com.elypia.cmdlrdiscord.DiscordController;
import com.elypia.commandler.CommandlerEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.util.Objects;

public final class DiscordUtils {

	private static final Logger logger = LoggerFactory.getLogger(DiscordUtils.class);

	/** The bot invite URL template, append the id of the bot for a full invite link. */
	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=";

	/** The common name for the default or public channel in a Guild. */
	private static final String COMMON_DEFAULT = "general";

	private DiscordUtils() {
		// Don't construct this
	}

	/**
	 * Check if this is a Discord event, and if so if it
	 * has a reference to a guild and returns calls
	 * {@link #newEmbed(Guild)}.
	 *
	 * @param event The {@link CommandlerEvent} that wants this new embed.
	 * @return A new embed builder, if guild is non-null, it may have a color set.
	 */
	public static EmbedBuilder newEmbed(CommandlerEvent<Event, Message> event) {
		if (event.getController() instanceof DiscordController)
			throw new RuntimeException("Embeds are specific to Discord only.");

		Object source = event.getSource();

		if (source instanceof GenericGuildEvent)
			return newEmbed(((GenericGuildEvent)source).getGuild());

		if (source instanceof GenericMessageEvent) {
			GenericMessageEvent gme = (GenericMessageEvent)source;

			if (gme.isFromGuild())
				return newEmbed(gme.getGuild());
		}

		return newEmbed((Guild)null);
	}

	/**
	 * Returns a new embed, colors the embed in the same color as the bots
	 * role if guild is non-null and the bot has a colored role.
	 *
	 * @param guild The guild this embed will be sent in.
	 * @return A new embed builder.
	 */
	public static EmbedBuilder newEmbed(Guild guild) {
		EmbedBuilder builder = new EmbedBuilder();

		if (guild != null)
			builder.setColor(guild.getSelfMember().getColor());

		return builder;
	}

	/**
	 * Find the most appropriate channel to write a message in.
	 * This could be #general, the default channel, a random channel.
	 *
	 * @param guild The guild to search through.
	 * @return A channel the bot should send messages to, or null if the
	 * bot literally can not talk anywhere.
	 * @throws NullPointerException If the guild is null.
	 */
	public static TextChannel getWriteableChannel(Guild guild) {
		Objects.requireNonNull(guild);

		TextChannel channel = guild.getDefaultChannel();

		if (channel != null && channel.canTalk())
			return channel;

		for (TextChannel iter : guild.getTextChannels()) {
			if (iter.getName().equalsIgnoreCase(COMMON_DEFAULT) && iter.canTalk())
				return iter;

			if (channel == null && iter.canTalk())
				channel = iter;
		}

		if (channel == null)
			logger.warn("#getWriteableChannel couldn't find any appropriate channel to write in.");

		return channel;
	}

	/**
	 * @param user The bot this get the invite link for.
	 * @return The invite link for this bot.
	 * @throws NullPointerException If the user is null.
	 * @throws RuntimeException If the {@link User} specified is not a bot.
	 */
	public static String getInviteUrl(User user) {
		Objects.requireNonNull(user);

		if (!user.isBot())
			throw new RuntimeException("User is not a bot.");

		return BOT_URL + user.getId();
	}
}
