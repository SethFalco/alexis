package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(name = "National Novel Writing Month", aliases = {"nanowrimo", "nano", "nnwm"}, description = "")
public class NanowrimoHandler extends CommandHandler {

	private MongoDatabase database;
	private Nanowrimo nanowrimo;

	public NanowrimoHandler() {
		this.database = database;
		nanowrimo = new Nanowrimo();
	}

	@Override
	public boolean test() {
		return false;
	}

	@Command(name = "Authenticate to NaNoWriMo", aliases = {"authenticate", "auth"}, help = "Auth to your NaNoWriMo account.")
	@Param(name = "name", help = "Your NaNoWriMo username.")
	@Param(name = "secret", help = "Your NaNoWriMo secret at: https://nanowrimo.org/api/wordcount")
	@Param(name = "wordcount", help = "Your total word count to submit.")
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
		}, failure -> BotUtils.sendHttpError(event, failure));
	}

	@Command(name = "Writer Info", aliases = "info", help = "Get basic information on a user.")
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

			event.reply(builder);
		}, failure -> BotUtils.sendHttpError(event, failure));
	}
}
