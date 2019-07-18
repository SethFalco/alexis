package com.elypia.alexis.modules.discord;

import com.elypia.alexis.utils.*;
import com.elypia.cmdlrdiscord.Scope;
import com.elypia.cmdlrdiscord.annotations.Scoped;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Module(name = "user", group = "Discord", aliases = "user", help = "user.h")
public class UserModule implements Handler {

	private final LanguageInterface lang;

	@Inject
	public UserModule(LanguageInterface lang) {
		this.lang = lang;
	}

	@Command(name = "user.info", aliases = "info", help = "user.info.h")
	public EmbedBuilder getInfo(
		CommandlerEvent<MessageReceivedEvent> event,
		@Param(name = "p.user", help = "user.info.user", defaultValue = "${src.author.id}") @Scoped(inGuild = Scope.LOCAL, inPrivate = Scope.MUTUAL) User user
	) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getSource().getGuild();

		if (guild != null) {
			Member member = guild.getMember(user);
			builder.setAuthor(member.getEffectiveName());
			builder.addField("user.info.online_status", member.getOnlineStatus().toString(), true);
			builder.addField(lang.get("user.info.joined_guild", Map.of("guild", guild.getName())), member.getTimeJoined().format(format), true);

			Collection<Role> roles = member.getRoles();

			if (!roles.isEmpty()) {
				StringJoiner joiner = new StringJoiner(", ");
				member.getRoles().forEach(o -> joiner.add(o.getName()));
				builder.addField(lang.get("user.info.roles", Map.of("roles", member.getRoles().size())), joiner.toString(), false);
			}
		} else {
			builder.setAuthor(user.getName());
		}

		builder.setThumbnail(avatar);
		builder.addField("user.info.joined_discord", user.getTimeCreated().format(format), true);

		if (user.isBot())
			builder.addField("user.info.bot", Md.a("common.invite_link", DiscordUtils.getInviteUrl(user)), false);

		builder.setFooter(lang.get("user.info.id", Map.of("id", user.getId())), null);

		return builder;
	}
}
