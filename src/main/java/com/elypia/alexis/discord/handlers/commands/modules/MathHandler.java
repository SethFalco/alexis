package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.utils.math.MathUtils;

@Module (
	aliases = {"Math"},
	help = "Math commands and fun stuff."
)
public class MathHandler extends CommandHandler {

	@Command (aliases = {"convert"}, help = "Convert a number to it's written equivelent.")
	@Parameter(name = "value", help = "The number to convert to the written form.")
	public void asWritten(MessageEvent event, long value) {
		String result = MathUtils.asWritten(value);

		if (result == null)
			event.reply("Sorry, I was unable to convert that!");
		else
			event.reply(result);
	}
}
