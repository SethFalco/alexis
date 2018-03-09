package com.elypia.alexis.discord;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.BotUtils;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.logging.Level;

public class DiscordDispatcher extends ListenerAdapter {

	private final Chatbot chatbot;
	private final ChatbotConfiguration config;

	public DiscordDispatcher(final Chatbot chatbot) {
		this.chatbot = chatbot;
		this.config = chatbot.getConfig();
	}

	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
		BotUtils.LOGGER.log(Level.INFO, "Time taken to launch: {0}ms", timeElapsed);

		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

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

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (config.developersOnly() && !config.isDeveloper(event.getAuthor()))
			return;

		if (event.getAuthor().isBot())
			return;

		chatbot.handleMessage(event);
	}
}
