package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.Static;
import com.elypia.elypiai.utils.math.MathUtils;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	name = "Miscellaneous Utilities",
	aliases = {"util", "math"},
	description = "Math commands and fun stuff."
)
public class UtilHandler extends CommandHandler {

	@Command(aliases = {"convert"}, help = "Convert a number to it's written equivelent.")
	@Param(name = "value", help = "The number to convert to the written form.")
	public EmbedBuilder asWritten(long values[]) {
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

		return builder;
	}

	@Static
	@Command(aliases = "count", help = "Cound the number of characters sent.")
	@Param(name = "text", help = "The text to count from.")
	public String count(String input) {
		return String.format("There are %,d characters in the input text.", input.length());
	}
}
