package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.annotations.Scope;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.StringJoiner;

import static com.elypia.alexis.utils.BotUtils.inviteUrl;

@Module(
	aliases = "User",
	help = "Get information or stats on global users! Try 'members' for guild specific commands."
)
public class UserHandler extends CommandHandler {

	@Command(aliases = "info", help = "Get some basic information on the user!")
	public void getInfo(MessageEvent event) {
		getInfo(event, event.getAuthor());
	}

	@Command(aliases = "info", help = "Get some basic information on the user!")
	@Parameter(name = "user", help = "The user to display information for.")
	@Scope(ChannelType.TEXT)
	public void getInfo(MessageEvent event, User user) {
		EmbedBuilder builder = new EmbedBuilder();
		Member member = event.getMember();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;

		builder.setAuthor(member.getEffectiveName());
		builder.setThumbnail(avatar);
		builder.addField("Online Status", member.getOnlineStatus().toString(), true);
		builder.addField("Status", member.getGame().getName(), true);
		builder.addField("Joined Discord", user.getCreationTime().format(format), true);
		builder.addField("Joined " + event.getGuild().getName(), member.getJoinDate().format(format), true);

		Collection<Role> roles = member.getRoles();

		if (!roles.isEmpty()) {
			StringJoiner joiner = new StringJoiner(", ");
			member.getRoles().forEach(o -> joiner.add(o.getName()));
			builder.addField("Roles", joiner.toString(), false);
		}

		if (user.isBot())
			builder.setFooter(Markdown.a("Invite me!", inviteUrl(user)), avatar);
		else
			builder.setFooter("ID: " + user.getId(), null);

		event.reply(builder);
	}
}
