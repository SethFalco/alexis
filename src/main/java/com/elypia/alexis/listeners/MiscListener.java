/*
 * Copyright (C) 2019-2019  Elypia
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

package com.elypia.alexis.listeners;

import com.elypia.alexis.ChatBot;
import com.elypia.alexis.configuration.DiscordConfig;
import com.elypia.alexis.utils.DiscordUtils;
import com.google.inject.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Handles misc actions Alexis should perform.
 */
@Singleton
public class MiscListener extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MiscListener.class);

	private final DiscordConfig discordConfig;

	@Inject
	public MiscListener(final DiscordConfig discordConfig) {
		this.discordConfig = discordConfig;
	}

	/**
	 * Log the time taken to startup and set the online status to {@link OnlineStatus#ONLINE}.
	 *
	 * @param event ReadyEvent
	 */
	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - ChatBot.START_TIME;
		String timeElapsedText = String.format("%,d", timeElapsed);
		logger.info("Time taken to launch: {}ms", timeElapsedText);
		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 * If the bot joined more than 10 minutes ago, we just ignore the event as
	 * sometimes Discord just gives us the event for no reason.
	 *
	 * @param event GuildJoinEvent
	 */
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();

		if (guild.getSelfMember().getTimeJoined().isBefore(OffsetDateTime.now().minusMinutes(10)))
			return; // We didn't actually join this guild now.

		TextChannel channel = DiscordUtils.getWriteableChannel(event);

		if (channel == null)
			return; // We can't write into any of the channels.

		String prefix = discordConfig.getPrefix().get(0);
		String message = "Thank you for inviting me! My default prefix is `" + prefix + "` but you can mention me too!\nFeel free to try my help command!";
		channel.sendMessage(message).queue();

		logger.info("The guild {} just invited me! ({})", guild.getName(), statsMessage(event.getJDA()));
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		logger.info("The guild {} just kicked me! ({})", event.getGuild().getName(), statsMessage(event.getJDA()));
	}

	/**
	 * @param jda The JDA instance that has joined or left a guild.
	 * @return The stats for this bot.
	 */
	private String statsMessage(JDA jda) {
		final String MESSAGE = "%,d guilds | %,d users | %,d bots!";

		int guildCount = jda.getGuilds().size();
		List<User> users = jda.getUsers();
		long botCount = users.stream().filter(User::isBot).count();
		long userCount = users.size() - botCount;

		return String.format(MESSAGE, guildCount, userCount, botCount);
	}
}
