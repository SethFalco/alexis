package com.elypia.alexis.discord.commands;

import com.elypia.alexis.discord.commands.annotation.Command;

public class CommandHandler {
	
	@Command(
		alias = "help",
		help = "Displays all help information for commands in the module."
	)
	public void help(CommandEvent event) {
		
	}
}
