package com.elypia.alexis.discord.handlers.commands.impl;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.utils.BotUtils;

import java.util.Collection;

public abstract class CommandHandler {

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	public boolean test() {
		return true;
	}

	/**
	 * @param event
	 * @return 	If we should continue to process the command.
	 */

	public boolean beforeAny(MessageEvent event) {
		return true;
	}

	@Command(
		aliases = "help",
		help = "Displays all help information for commands in the module."
	)
	public void help(MessageEvent event) {
		Module module = BotUtils.getModule(this);
		Collection<Command> commands = BotUtils.getCommands(this);

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

	public boolean isEnabled() {
		return enabled;
	}
}
