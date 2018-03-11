package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.CommandEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

@Module(
	aliases = "User",
	help = "Get information or stats on global users! Try 'members' for guild specific commands."
)
public class UserHandler extends CommandHandler {

	@Command (
		aliases = "info",
		help = "Get some basic information on the user!"
	)
	public void getInfo(CommandEvent event) {
		User user = event.getAuthor();
		Member member = event.getMember();

		EmbedBuilder builder = new EmbedBuilder();

		builder.addField("Join Date", member.getJoinDate().toString(), false);

		event.reply(builder);
	}
}
