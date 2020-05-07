/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.comcord.*;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import java.util.Objects;

/**
 * Utility methods to put reusable methods to interact with JDA.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public final class DiscordUtils {

	private static final Logger logger = LoggerFactory.getLogger(DiscordUtils.class);

	/** The bot invite URL template, append the id of the bot for a full invite link. */
	private static final String BOT_URL = "https://discord.com/oauth2/authorize?scope=bot&client_id=";

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
	 * @param event The {@link ActionEvent} that wants this new embed.
	 * @return A new embed builder, if guild is non-null, it may have a color set.
	 */
	public static EmbedBuilder newEmbed(ActionEvent<?, ?> event) {
		if (!(event.getRequest().getIntegration() instanceof DiscordIntegration))
			throw new RuntimeException("Embeds are specific to Discord only.");

		Object source = event.getRequest().getSource();

		if (source instanceof Event) {
			Guild guild = EventUtils.getGuild((Event)source);
			return newEmbed(guild);
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
