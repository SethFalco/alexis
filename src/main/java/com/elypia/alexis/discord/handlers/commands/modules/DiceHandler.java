package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.discord.minigames.Dicing;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

@Module (
	aliases = {"dice", "dicing"},
	help = "A dicing minigame to put people at a test of luck."
)
public class DiceHandler extends CommandHandler {

	private Map<Guild, Dicing> instances;

	public DiceHandler() {
		instances = new HashMap<>();
	}

	@Command (
		aliases = "create",
		help = "Create an instance of the game, dicing for users to join."
	)
	public void create(MessageEvent event) {
		instances.put(event.getGuild(), new Dicing());
	}

	public void join(MessageEvent event) {

	}

	public void start(MessageEvent event) {

	}
}
