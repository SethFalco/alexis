package com.elypia.alexis.discord.handlers.commands.impl;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.events.CommandEvent;

public abstract class CommandHandler {

	public abstract boolean test();

	@Command(
		aliases = "help",
		help = "Displays all help information for commands in the module."
	)
	public void help(CommandEvent event) {

	}
}
