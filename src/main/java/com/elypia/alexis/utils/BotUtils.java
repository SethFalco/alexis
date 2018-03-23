package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotUtils {

	private BotUtils() {
		// Unable to instantiate this class.
	}

	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	public static void unirestFailure(MessageEvent event, IOException failure) {
		// Log the exception to console, was likely time out or API in use is deprecated.
		LOGGER.log(Level.SEVERE, "Unirest request failed.", failure);

		// Let the user know what happened and apologise.
		String message = "Sorry! I'm don't know why the command failed but I'm reporting this to Seth, perhaps trying again later?";
		event.getChannel().sendMessage(message).queue();
	}

	public static Module getModule(CommandHandler handler) {
		return handler.getClass().getAnnotation(Module.class);
	}

	public static Collection<Command> getCommands(CommandHandler handler) {
		Collection<Command> commands = new ArrayList<>();
		Method[] methods = handler.getClass().getMethods();

		for (Method method : methods) {
			Command command = method.getAnnotation(Command.class);

			if (command != null)
				commands.add(command);
		}

		return commands;
	}

	public static Member findMember(Guild guild, String term) {
		if (guild == null || term == null)
			return null;

		List<Member> members = guild.getMembers();
		Set<Member> results = new HashSet<>();

		members.forEach(member -> {
			Member m = guild.getMemberById(term);

			if (m != null)
				results.add(m);

			if (member.getNickname().equalsIgnoreCase(term))
				results.add(m);

			if (member.getAsMention().equals(term))
				results.add(m);

			if (member.getUser().getName().equalsIgnoreCase(term))
				results.add(m);

		});

		return null;
	}
}
