package com.elypia.alexis.discord;

import java.util.logging.Level;

import com.elypia.alexis.*;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordDispatcher extends ListenerAdapter {
	
	private Chatbot chatbot;
	
	public DiscordDispatcher(final Chatbot chatbot) {
		this.chatbot = chatbot;
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
		AlexisUtils.LOGGER.log(Level.INFO, "Time taken to launch minimally: {0}ms", timeElapsed);
		
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
		if (event.getAuthor().isBot())
			return;
		
		chatbot.handleMessage(event);
	}
}
