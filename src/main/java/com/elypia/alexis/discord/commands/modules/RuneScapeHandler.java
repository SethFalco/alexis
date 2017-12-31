package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.*;
import com.elypia.alexis.discord.commands.impl.CommandHandler;

@Module (
	aliases = {"RuneScape", "RS"},
	description = "Integration with the popular MMORPG, RuneScape!"
)
public class RuneScapeHandler extends CommandHandler {

	@Override
	public boolean test() {

		return false;
	}
	
	@Command (
		aliases = "status",
		help = "The total number of created accounts."
	)
	public void displayStatus(CommandEvent event) {
		
	}
	
	@Command (
		aliases = "stats",
		help = "Get stats for a particular user.",
		params = {
			@Parameter(param = "username", help = "RuneScape players username.", type = String.class)
		}
	)
	public void getPlayerStats(CommandEvent event) {
		
	}
}
