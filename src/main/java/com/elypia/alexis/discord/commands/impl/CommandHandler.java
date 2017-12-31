package com.elypia.alexis.discord.commands.impl;

import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.Command;

public abstract class CommandHandler {
	
	public abstract boolean test();
	
	@Command(
		aliases = "help",
		help = "Displays all help information for commands in the module."
	)
	public void help(CommandEvent event) {
		
	}
}
