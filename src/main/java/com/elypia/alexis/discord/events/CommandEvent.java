package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.annotation.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEvent extends GenericEvent {

	private static final String COMMAND_REGEX = "(?i)^(?<prefix><@!?%s> {0,2}|%s)(?<module>[A-Z]+)(?:\\.(?<submodule>[A-Z]+))?(?: (?<command>[A-Z]+))(?: (?<params>.*))?";
	private static final String PARAM_REGEX = "\\b(?<=\").+?(?=\")|[^\\s\"]+";

	private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

	private MessageReceivedEvent event;

	private boolean isValid;
	private String prefix;
	private String module;
	private String submodule;
	private String command;
	private String[] params;
	private String[] optParams;

	private Method method;
	private Command annotation;
	private String[] reactions;

	private Message reply;

	public CommandEvent(Chatbot chatbot, MessageReceivedEvent event) {
		this(chatbot, event, event.getMessage().getContentRaw());
	}

	public CommandEvent(Chatbot chatbot, MessageReceivedEvent event, String content) {
		super(chatbot, event);

		String prefix;

		if (chatbot.getConfig().enforcePrefix())
			prefix = chatbot.getConfig().getDefaultPrefix();
		else
			prefix = ">";

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

			while (matcher.find())
				matches.add(matcher.group());

			params = matches.toArray(new String[matches.size()]);
		}
	}

	// Functions

	public void reply(String body) {
		getChannel().sendMessage(body).queue(this::afterReply);
	}

	public void reply(EmbedBuilder builder) {
		reply(builder, null);
	}

	public void reply(EmbedBuilder builder, Consumer<Message> consumer) {
		Guild guild = getGuild();

		if (guild != null) {
			Color color = guild.getSelfMember().getColor();
			builder.setColor(color);
		}

		getChannel().sendMessage(builder.build()).queue(msg -> {
			if (consumer != null)
				consumer.accept(msg);

			afterReply(msg);
		});
	}

	private void afterReply(Message message) {
		reply = message;

		for (String reaction : reactions)
			message.addReaction(reaction).queue();
	}

	public void tryDeleteMessage() {
		if (canDeleteMessage())
			getMessage().delete().queue();
	}

	public boolean canDeleteMessage() {
		if (getChannel().getType() == ChannelType.TEXT)
			return getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE);

		return getAuthor() == getJDA().getSelfUser();
	}

	// Getters and setters

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

	public Method getMethod() {
		return method;
	}

	public Command getCommandAnnotation() {
		return annotation;
	}

	public String[] getReactions() {
		return reactions;
	}

	public void setMethod(Method method) {
		this.method = method;
		this.annotation = method.getAnnotation(Command.class);
		this.reactions = annotation.reactions();
	}

	public Message getReply() {
		return reply;
	}

	// JDA

	public JDA getJDA() {
		return event.getJDA();
	}

	public Member getSelfMember() {
		return event.getGuild().getSelfMember();
	}

	public MessageReceivedEvent getMessageEvent() {
		return event;
	}

	public Guild getGuild() {
		return event.getGuild();
	}

	public MessageChannel getChannel() {
		return event.getChannel();
	}

	public User getAuthor() {
		return event.getAuthor();
	}

	public Member getMember() {
		return event.getMember();
	}

	public Message getMessage() {
		return event.getMessage();
	}
}
