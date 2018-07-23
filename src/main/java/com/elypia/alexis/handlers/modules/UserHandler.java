package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.annotations.validation.param.Search;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Module(name = "User", aliases = "user", help = "Get information or stats on global users! Try 'members' for guild specific commands.")
public class UserHandler extends JDAHandler {

	@Overload(value = 5, params = {})
	public void getInfo(JDACommand event) {
		getInfo(event, event.getMessage().getAuthor());
	}

	@Command(id = 5, name = "User Info", aliases = "info", help = "Get some basic information on the user!")
	@Param(name = "user", help = "The user to display information for.")
	@Scope(ChannelType.TEXT)
	public EmbedBuilder getInfo(JDACommand event, @Search(SearchScope.LOCAL) User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getSource().getGuild();

		if (guild != null) {
			Member member = guild.getMember(user);
			builder.setAuthor(member.getEffectiveName());
			builder.addField("Online Status", member.getOnlineStatus().toString(), true);
			builder.addField("Status", member.getGame().getName(), true);
			builder.addField("Joined " + guild.getName(), member.getJoinDate().format(format), true);

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
			builder.addField("Bot", Markdown.a("Invite link!", BotUtils.formInviteUrl(user)), false);

		builder.setFooter("ID: " + user.getId(), null);

		return builder;
	}
}
