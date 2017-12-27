package com.elypia.alexis.discord.commands;

import java.util.regex.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandEvent {
	
	public static final String COMMAND_REGEX = "(?i)^<@!?%s>|%s ?(?<module>[A-Z]+) (?<command>[A-Z]+)";
	
	private MessageReceivedEvent event;
	private boolean isValid;
	private String module;
	private String command;
	private String[] params;
	private String[] optParams;
	
	public CommandEvent(MessageReceivedEvent event) {
		this(event, event.getMessage().getContentRaw());
	}
	
	public CommandEvent(MessageReceivedEvent event, String content) {		
		this.event = event;
		
		String prefix = ">";
		String id = event.getJDA().getSelfUser().getId();
		String regex = String.format(COMMAND_REGEX, id, prefix);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		
		isValid = matcher.matches();
		
		if (!isValid)
			return;
		
		module = matcher.group("module").toLowerCase();
		command = matcher.group("command").toLowerCase();
	}
	
	public MessageReceivedEvent getMessageReceivedEvent() {
		return event;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getModule() {
		return module;
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
