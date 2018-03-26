package com.elypia.alexis.discord.handlers.impl;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public abstract class CommandHandler {

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean test() {
		return true;
	}

	@Command(
		aliases = "help",
		help = "Displays all help information for commands in the module."
	)
	public void help(MessageEvent event) {
		Module module = getClass().getAnnotation(Module.class);
		Collection<Command> commands = getCommands(this);

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("** %s**\n", module.aliases()[0]));
		builder.append(module.help() + "\n");

		for (Command command : commands) {
			if (!command.help().isEmpty()) {
				builder.append(String.format("`%s`: %s\n", command.aliases()[0], command.help()));
			}
		}

		event.reply(builder.toString());
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

	public boolean isEnabled() {
		return enabled;
	}
}
