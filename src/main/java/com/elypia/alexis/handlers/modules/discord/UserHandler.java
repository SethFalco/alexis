package com.elypia.alexis.handlers.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Module(name = "user", group = "Discord", aliases = "user", help = "user.h")
public class UserHandler extends JDAHandler {

	@Overload(value = 5, params = {})
	public void getInfo(JDACommand event) {
		getInfo(event, event.getMessage().getAuthor());
	}

	@Command(id = 5, name = "user.info", aliases = "info", help = "user.info.h")
	@Param(name = "common.user", help = "user.info.p.user.h")
	public EmbedBuilder getInfo(JDACommand event, @Search(Scope.LOCAL) User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getSource().getGuild();

		if (guild != null) {
			Member member = guild.getMember(user);
			builder.setAuthor(member.getEffectiveName());
			builder.addField("user.info.online_status", member.getOnlineStatus().toString(), true);
			builder.addField("user.info.status", member.getGame().getName(), true);
			builder.addField(event.getScript("user.info.joined_guild", Map.of("guild", guild.getName())), member.getJoinDate().format(format), true);

			Collection<Role> roles = member.getRoles();

			if (!roles.isEmpty()) {
				StringJoiner joiner = new StringJoiner(", ");
				member.getRoles().forEach(o -> joiner.add(o.getName()));
				builder.addField(event.getScript("user.info.roles", Map.of("roles", member.getRoles().size())), joiner.toString(), false);
			}
		} else {
			builder.setAuthor(user.getName());
		}

		builder.setThumbnail(avatar);
		builder.addField("user.info.joined_discord", user.getCreationTime().format(format), true);

		if (user.isBot())
			builder.addField("user.info.bot", Markdown.a("common.invite_link", BotUtils.getInviteUrl(user)), false);

		builder.setFooter(event.getScript("user.info.id", Map.of("id", user.getId())), null);

		return builder;
	}
}
