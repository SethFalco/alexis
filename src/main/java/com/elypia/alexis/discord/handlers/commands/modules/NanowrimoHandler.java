package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Database;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	aliases = {"NaNoWriMo", "nano", "nnwm"},
	help = ""
)
public class NanowrimoHandler extends CommandHandler {

	private MongoDatabase database;
	private Nanowrimo nanowrimo = new Nanowrimo();

	public NanowrimoHandler(MongoDatabase database) {
		this.database = database;
	}

	@Override
	public boolean test() {

		return false;
	}

	@Command(aliases = {"authenticate", "auth"}, help = "Auth to your NaNoWriMo account.")
	@Parameter(name = "name", help = "Your NaNoWriMo username.")
	@Parameter(name = "secret", help = "Your NaNoWriMo secret at: https://nanowrimo.org/api/wordcount", secret = true)
	@Parameter(name = "wordcount", help = "Your total word count to submit.")
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
	@Parameter(name = "name", help = "Your NaNoWriMo username.")
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
