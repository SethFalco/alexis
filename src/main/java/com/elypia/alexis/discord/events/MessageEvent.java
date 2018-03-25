package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.Config;
import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.PostReactions;
import com.elypia.alexis.discord.events.impl.GenericEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
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

public class MessageEvent extends GenericEvent {

	private static final String COMMAND_REGEX = "(?i)^(?<prefix><@!?%s>|%s)\\s{0,2}(?<module>[A-Z]+)(?:\\.(?<submodule>[A-Z]+))?(?: (?<command>[A-Z]+))?(?: (?<params>.*))?";
	private static final Pattern PARAM_PATTERN = Pattern.compile("(?<quotes>\\b(?<=\")(?:\\\\\"|[^\"])*(?=\"))|(?<args>[^\\s\",]+(?:\\s*,\\s*[^\\s\",]+)*)");

	private MessageReceivedEvent event;

	private boolean isValid;
	private String prefix;
	private String module;
	private String submodule;
	private String command;
	private List<Object> params;

	private Method method;
	private Command annotation;

	private Message reply;

	public MessageEvent(Chatbot chatbot, MessageReceivedEvent event) {
		this(chatbot, event, event.getMessage().getContentRaw());
	}

	public MessageEvent(Chatbot chatbot, MessageReceivedEvent event, String content) {
		super(chatbot, event);
		this.event = event;
		params = new ArrayList<>();

		String prefix;

		if (Config.enforcePrefix)
			prefix = Config.defaultPrefix;
		else
			prefix = ">";

		String id = event.getJDA().getSelfUser().getId();
		String regex = String.format(COMMAND_REGEX, id, prefix);

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		isValid = matcher.matches();

		if (!isValid)
			return;

		prefix = matcher.group("prefix");
		module = matcher.group("module").toLowerCase();

		submodule = matcher.group("submodule");

		if (submodule != null)
			submodule = submodule.toLowerCase();

		command = matcher.group("command");

		if (command != null)
			command = command.toLowerCase();

		String parameters = matcher.group("params");

		// Due to change.
		if (parameters != null) {
			matcher = PARAM_PATTERN.matcher(parameters);

			while (matcher.find()) {
				String quotes = matcher.group("quotes");
				String args = matcher.group("args");

				if (quotes != null)
					params.add(quotes);

				else if (args != null) {
					String[] array = args.split("\\s*,\\s*");

					if (array.length == 1)
						params.add(array[0]);
					else
						params.add(array);
				}
			}
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

		PostReactions post = method.getAnnotation(PostReactions.class);

		if (post != null) {
			for (String reaction : post.value())
				message.addReaction(reaction).queue();
		}
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

	public void commit() {
		if (guildData != null)
			guildData.commit();

		if (userData != null)
			userData.commit();
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

	public void setCommand(CommandHandler handler) {
		Module module = handler.getClass().getAnnotation(Module.class);
		this.command = module.defaultCommand();
	}

	public List<Object> getParams() {
		return params;
	}

	public Method getMethod() {
		return method;
	}

	public Command getCommandAnnotation() {
		return annotation;
	}

	public void setMethod(Method method) {
		this.method = method;
		this.annotation = method.getAnnotation(Command.class);
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
