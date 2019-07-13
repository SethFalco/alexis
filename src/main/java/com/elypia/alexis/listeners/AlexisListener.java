package com.elypia.alexis.listeners;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.database.entities.GuildData;
import com.elypia.alexis.utils.BotUtils;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.*;

import java.time.OffsetDateTime;
import java.util.List;

public class AlexisListener extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AlexisListener.class);

	/**
	 * @param event ReadyEvent
	 */
	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
		logger.info("Time taken to launch: {}ms", String.format("%,d", timeElapsed));

		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 *
	 * @param event GuildJoinEvent
	 */

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();

		if (guild.getSelfMember().getTimeJoined().isBefore(OffsetDateTime.now().minusMinutes(10)))
			return;

		TextChannel channel = BotUtils.getWriteableChannel(event);

		if (channel == null)
			return;

		String prefix = Alexis.configurationService.getDiscordConfig().getPrefix();
		String message = "Thank you for inviting me! My default prefix is `" + prefix + "` but you can mention me too!\nFeel free to try my help command!";
		channel.sendMessage(message).queue();

		logger.info("The guild {} just invited me!\n{}", guild.getName(), statsMessage(event.getJDA()));

		GuildData data = new GuildData();
		data.setGuildId(guild.getIdLong());
		Alexis.getDatabaseManager().commit(data);
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Guild guild = event.getGuild();
		logger.info("The guild %s just kicked me!\n{}", guild.getName(), statsMessage(event.getJDA()));
	}

	private String statsMessage(JDA jda) {
		final String MESSAGE = "I'm now in %,d guilds, totalling %,d users, and %,d bots!";

		int guildCount = jda.getGuilds().size();

		List<User> users = jda.getUsers();
		long botCount = users.stream().filter(User::isBot).count();
		long userCount = users.size() - botCount;

		return String.format(MESSAGE, guildCount, userCount, botCount);
	}
}
