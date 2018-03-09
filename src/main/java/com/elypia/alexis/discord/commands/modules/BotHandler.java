package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.OptParameter;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

@Module (
	aliases = {"Bot", "Robot"},
	help = "Bot commands for stats or information."
)
public class BotHandler extends CommandHandler {

	@Override
	public boolean test() {

		return false;
	}

	@Command (
		aliases = "ping",
		help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!"
	)
	public void ping(CommandEvent event) {
		pingPong(event, "pong!");
	}

	@Command (
		aliases = "pong",
		help = ""
	)
	public void pong(CommandEvent event) {
		pingPong(event, "ping!");
	}

	private void pingPong(CommandEvent event, String text) {
		long startTime = System.currentTimeMillis();

		event.getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;

			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}

	@Command (
		aliases = {"stats", "info"},
		help = "Display stats on Alexis!"
	)
	public void displayStats(CommandEvent event) {
		JDA jda = event.getJDA();
		User alexis = jda.getSelfUser();

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(String.format("%s stats!", alexis.getName()));
		builder.setThumbnail(alexis.getAvatarUrl());
		builder.addField("Guilds", String.valueOf(jda.getGuilds().size()), false);
		builder.addField("Users", String.valueOf(jda.getUsers().size()), false);

		event.reply(builder);
	}

	@Command (
		aliases = "say",
		help = "Have Alexis repeat something you say!",
		params = {
			@Parameter (
				param = "body",
				help = "Text Alexis should repeat!",
				type = String.class
			)
		},
		optParams = {
			@OptParameter (
				param = "delete",
				help = "Should Alexis delete the message after?",
				type = Boolean.class,
				defaultValue = "false"
			)
		}
	)
	public void say(CommandEvent event) {
		event.tryDeleteMessage();
		event.reply(event.getParams()[0]);
	}
}
