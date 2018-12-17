package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Module(id = "Users", group = "Discord", aliases = "user", help = "user.h")
public class UserModule extends JDACHandler {

	@Command(id = "User Info", aliases = "info", help = "user.info.h")
	@Param(id = "common.user", help = "user.info.p.user.h")
	public EmbedBuilder getInfo(JDACEvent event, @Search(Scope.LOCAL) User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getSource().getGuild();

		if (guild != null) {
			Member member = guild.getMember(user);
			builder.setAuthor(member.getEffectiveName());
			builder.addField("user.info.online_status", member.getOnlineStatus().toString(), true);
			builder.addField("user.info.status", member.getGame().getName(), true);
			builder.addField(scripts.get("user.info.joined_guild", Map.of("guild", guild.getName())), member.getJoinDate().format(format), true);

			Collection<Role> roles = member.getRoles();

			if (!roles.isEmpty()) {
				StringJoiner joiner = new StringJoiner(", ");
				member.getRoles().forEach(o -> joiner.add(o.getName()));
				builder.addField(scripts.get("user.info.roles", Map.of("roles", member.getRoles().size())), joiner.toString(), false);
			}
		} else {
			builder.setAuthor(user.getName());
		}

		builder.setThumbnail(avatar);
		builder.addField("user.info.joined_discord", user.getCreationTime().format(format), true);

		if (user.isBot())
			builder.addField("user.info.bot", Md.a("common.invite_link", BotUtils.getInviteUrl(user)), false);

		builder.setFooter(scripts.get("user.info.id", Map.of("id", user.getId())), null);

		return builder;
	}

	@Overload(value = "User Info", params = {})
	public void getInfo(JDACEvent event) {
		getInfo(event, ((MessageReceivedEvent)event.getSource()).getMessage().getAuthor());
	}
}
