package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.utils.math.MathUtils;
import net.dv8tion.jda.core.EmbedBuilder;

@Module (
	aliases = {"Math"},
	help = "Math commands and fun stuff."
)
public class MathHandler extends CommandHandler {

	@Command (aliases = {"convert"}, help = "Convert a number to it's written equivelent.")
	@Parameter(name = "value", help = "The number to convert to the written form.")
	public void asWritten(MessageEvent event, long values[]) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**Results**");

		for (long l : values) {
			StringBuilder sb = new StringBuilder();
			sb.append("**  Input**\n```");
			sb.append(l);
			sb.append("```\n** Output**\n```");

			String result = MathUtils.asWritten(l);
			sb.append(result == null ? "Error" : result);

			sb.append("```");

			builder.addField("", sb.toString(), false);
		}

		event.reply(builder);
	}
}
