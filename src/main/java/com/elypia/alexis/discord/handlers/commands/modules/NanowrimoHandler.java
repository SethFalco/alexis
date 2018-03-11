package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
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

	@Command (
		aliases = {"authenticate", "auth"},
		help = "Authenticate to your NaNoWriMo account to prove who you are.",
		params = {
			@Parameter (
				param = "name",
				help = "Your NaNoWriMo username.",
				type = String.class
			),
			@Parameter (
				param = "secret",
				help = "Your NaNoWriMo secret, obtainable at: https://nanowrimo.org/api/wordcount",
				type = String.class,
				hidden = true
			),
			@Parameter (
				param = "wordcount",
				help = "Your total word count to submit.",
				type = Integer.class
			)
		},
		requiredDatabase = true
	)
	public void authenticate(MessageEvent event) {
		event.tryDeleteMessage();

		String[] params = event.getParams();

		nanowrimo.updateWordCount(params[1], params[0], Integer.parseInt(params[2]), result -> {
			switch (result) {
				case ERROR_NO_ACTIVE_EVENT: {

					event.reply("Well done, you've succesfully authenticated to your account.");
					break;
				}
				default: {
					event.reply("Sorry you failed to authenticate to your account, please check your details and try again.");
				}
			}
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Command (
		aliases = {"info"},
		help = "Get basic information on a user.",
		params = {
			@Parameter (
				param = "name",
				help = "Your NaNoWriMo username.",
				type = String.class
			)
		}
	)
	public void getUser(MessageEvent event) {
		nanowrimo.getNanoUser(event.getParams()[0], result -> {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(result.getUsername(), result.getProfileUrl());

			String fieldString = String.format("%,d", result.getWordCount());
			builder.addField("Total Word Count", fieldString, true);

			if (result.isWinner()) {
				String footerFormat = "%s is a winner of the last NaNoWriMo event!";
				builder.setFooter(String.format(footerFormat, result.getUsername()), null);
			}

			event.getChannel().sendMessage(builder.build()).queue();
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}
}
