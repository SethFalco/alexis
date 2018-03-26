package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.annotations.*;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
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

	@CommandGroup("info")
	public void getInfo(MessageEvent event) {
		getInfo(event, event.getAuthor());
	}

	@CommandGroup("info")
	@Command(aliases = "info", help = "Get some basic information on the user!")
	@Parameter(name = "user", help = "The user to display information for.")
	@Scope(ChannelType.TEXT)
	public void getInfo(MessageEvent event, User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Member member = event.getMember();

		if (member != null) {
			builder.setAuthor(member.getEffectiveName());
			builder.addField("Online Status", member.getOnlineStatus().toString(), true);
			builder.addField("Status", member.getGame().getName(), true);
			builder.addField("Joined " + event.getGuild().getName(), member.getJoinDate().format(format), true);

			Collection<Role> roles = member.getRoles();

			if (!roles.isEmpty()) {
				StringJoiner joiner = new StringJoiner(", ");
				member.getRoles().forEach(o -> joiner.add(o.getName()));
				builder.addField("Roles", joiner.toString(), false);
			}
		} else {
			builder.setAuthor(user.getName());
		}

		builder.setThumbnail(avatar);
		builder.addField("Joined Discord", user.getCreationTime().format(format), true);

		if (user.isBot())
			builder.addField("Bot", Markdown.a("Invite link!", inviteUrl(user)), false);

		builder.setFooter("ID: " + user.getId(), null);

		event.reply(builder);
	}
}
