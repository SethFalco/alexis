package com.elypia.alexis.discord.managers;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.utils.BotUtils;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.logging.Level;

public class EventManager extends ListenerAdapter {

    private Chatbot chatbot;

	public EventManager(Chatbot chatbot) {
		this.chatbot = chatbot;
	}

	/**
	 * Occurs when the bot succesfully logs in.
	 *
	 * @param event ReadyEvent
	 */

	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
		BotUtils.LOGGER.log(Level.INFO, "Time taken to launch: {0}ms", timeElapsed);

		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 *
	 * @param event GuildJoinEvent
	 */

	@Override
	public void onGuildJoin(GuildJoinEvent event) {

	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {

	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {

	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

	}

	@Override
	public void onGuildBan(GuildBanEvent event) {

	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {

	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {

	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {

	}
}
