package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.elypia.jdautils.annotations.access.Database;
import com.elypia.jdautils.annotations.command.Command;
import com.elypia.jdautils.annotations.command.Module;
import com.elypia.jdautils.annotations.command.Param;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	aliases = {"NaNoWriMo", "nano", "nnwm"},
	help = ""
)
public class NanowrimoHandler extends CommandHandler {

	private MongoDatabase database;
	private Nanowrimo nanowrimo;

	public NanowrimoHandler(MongoDatabase database) {
		this.database = database;
		nanowrimo = new Nanowrimo();
	}

	@Override
	public boolean test() {

		return false;
	}

	@Command(aliases = {"authenticate", "auth"}, help = "Auth to your NaNoWriMo account.")
	@Param(name = "name", help = "Your NaNoWriMo username.")
	@Param(name = "secret", help = "Your NaNoWriMo secret at: https://nanowrimo.org/api/wordcount", secret = true)
	@Param(name = "wordcount", help = "Your total word count to submit.")
	@Database
	public void authenticate(MessageEvent event, String name, String secret, int wordcount) {
		event.tryDeleteMessage();

		nanowrimo.updateWordCount(secret, name, wordcount, result -> {
			switch (result) {
				case ERROR_NO_ACTIVE_EVENT:
					event.reply("Well done, you've succesfully authenticated to your account.");
					break;

				default:
					event.reply("Sorry you failed to authenticate to your account, please check your details and try again.");
					break;
			}
		}, failure -> BotUtils.httpFailure(event, failure));
	}

	@Command(aliases = "info", help = "Get basic information on a user.")
	@Param(name = "name", help = "Your NaNoWriMo username.")
	public void getUser(MessageEvent event, String name) {
		nanowrimo.getNanoUser(name, result -> {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(result.getUsername(), result.getProfileUrl());

			String fieldString = String.format("%,d", result.getWordCount());
			builder.addField("Total Word Count", fieldString, true);

			if (result.isWinner()) {
				String footerFormat = "%s is a winner of the last NaNoWriMo event!";
				builder.setFooter(String.format(footerFormat, result.getUsername()), null);
			}

			event.getChannel().sendMessage(builder.build()).queue();
		}, failure -> BotUtils.httpFailure(event, failure));
	}
}
