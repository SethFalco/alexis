package com.elypia.alexis.discord.commands;

import java.util.*;
import java.util.regex.*;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandEvent {
	
	public static final String COMMAND_REGEX = "(?i)^(?<prefix><@!?%s> {0,2}|%s)(?<module>[A-Z]+)(?:\\.(?<submodule>[A-Z]+))? (?<command>[A-Z]+)(?: (?<params>.*))?";
	public static final String PARAM_REGEX = "\\b(?<=\").+?(?=\")|[^\\s\"]+";

	private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);
	
	private JDA jda;
	private Member selfMember;
	private MessageReceivedEvent event;
	private Guild guild;
	private MessageChannel channel;
	private User user;
	private Member member;
	private Message message;
	
	private boolean isValid;
	private String prefix;
	private String module;
	private String submodule;
	private String command;
	private String[] params;
	private String[] optParams;
	
	public CommandEvent(MessageReceivedEvent event) {
		this(event, event.getMessage().getContentRaw());
	}
	
	public CommandEvent(MessageReceivedEvent event, String content) {
		jda = event.getJDA();
		this.event = event;
		channel = event.getChannel();
		guild = event.getGuild();
		user = event.getAuthor();
		member = event.getMember();
		message = event.getMessage();
		
		if (guild != null)
			selfMember = guild.getSelfMember();
		
		String prefix = ">";
		String id = event.getJDA().getSelfUser().getId();
		String regex = String.format(COMMAND_REGEX, id, prefix);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		
		isValid = matcher.matches();
		
		if (!isValid)
			return;
		
		prefix = matcher.group("prefix").trim();
		module = matcher.group("module").toLowerCase();
		
		submodule = matcher.group("submodule");
		
		if (submodule != null)
			submodule = submodule.toLowerCase();
		
		command = matcher.group("command").toLowerCase();
		
		String parameters = matcher.group("params");
		
		// Due to change.
		if (parameters != null) {
			matcher = PARAM_PATTERN.matcher(parameters);
			List<String> matches = new ArrayList<>();
			
			while(matcher.find()) {
				matches.add(matcher.group());
			}
			
			params = matches.toArray(new String[matches.size()]);
		}
	}
	
	public void reply(String body) {
		channel.sendMessage(body).queue();
	}
	
	public void tryDeleteMessage() {
		if (canDeleteMessage())
			message.delete().queue();
	}
	
	public boolean canDeleteMessage() {
		if (channel.getType() == ChannelType.PRIVATE)
			return message.getAuthor() == jda.getSelfUser();
		
		return selfMember.hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE);
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	public Member getSelfMember() {
		return selfMember;
	}
	
	public MessageReceivedEvent getMessageEvent() {
		return event;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public MessageChannel getChannel() {
		return channel;
	}
	
	public User getAuthor() {
		return user;
	}
	
	public Member getMember() {
		return member;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getModule() {
		return module;
	}
	
	public String getSubmodule() {
		return submodule;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String[] getParams() {
		return params;
	}
	
	public String[] getOptParams() {
		return optParams;
	}
}
