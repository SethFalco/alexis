package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.commands.annotation.Module;
import com.elypia.alexis.discord.commands.impl.CommandHandler;

@Module(
	aliases = "user",
	description = "Get information or stats on global users! Try 'members' for guild specific commands."
)
public class UserHandler extends CommandHandler {

	@Override
	public boolean test() {

		return false;
	}
}
