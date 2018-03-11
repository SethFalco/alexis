package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.utils.math.IntToString;

@Module (
	aliases = {"Math"},
	help = "Math commands and fun stuff."
)
public class MathHandler extends CommandHandler {

	@Command (
		aliases = {"convert"},
		help = "Convert a number to it's written equivelent."
	)
	public void convertIntToString(MessageEvent event) {
		String result = IntToString.convert(event.getParams()[0]);

		if (result == null)
			event.reply("Sorry, I was unable to convert that!");
		else
			event.reply(result);
	}

	@Command (
		aliases = {"pi", "pie"},
		help = "Print pi~~e~~!"
	)
	public void displayPi(MessageEvent event) {
		event.reply(String.valueOf(Math.PI));
	}
}
