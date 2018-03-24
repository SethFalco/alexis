package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BotUtils {

	private BotUtils() {

	}

	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	public static void httpFailure(MessageEvent event, IOException failure) {
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
}
