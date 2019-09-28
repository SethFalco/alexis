/*
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.modules;

import com.google.inject.Inject;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import org.elypia.alexis.ChatBot;
import org.elypia.alexis.services.ConfigurationService;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.*;
import org.slf4j.*;

import javax.inject.Singleton;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Module(name = "bot", aliases = {"bot", "robot"}, help = "bot.help")
public class BotModule implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(BotModule.class);

	/**
	 * After this time bots had the same client id as user id which allows
	 * us to generate invite links for them, bots before this time however
	 * didn't have matching client/user ids and so their links would appear.
	 * dead. We have this here to make sure we only generate links for bots
	 * made after this time.
	 */
	private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

	private final LanguageInterface lang;

	private long ownerId;

	@Inject
	public BotModule(LanguageInterface lang) {
		this.lang = lang;
	}

	@Static
	@Command(name = "ping", aliases = "ping")
	public String ping() {
		return lang.get("bot.pong");
	}

	@Static
	@Command(name = "pong", aliases = "pong")
	public String pong() {
		return lang.get("bot.ping");
	}

	@Default
	@Command(name = "info", aliases = {"stats", "info"})
	public void displayStats(JDACEvent event) {
		GenericMessageEvent source = event.getSource();
		EmbedBuilder builder = DiscordUtils.newEmbed(source.getGuild());
		JDA jda = source.getJDA();
		User alexis = jda.getSelfUser();

		builder.setTitle(alexis.getName());
		builder.setDescription(scripts.get(source, "bot.description") + "\n_ _");
		builder.setThumbnail(alexis.getAvatarUrl());
		ConfigurationService configurationService = ChatBot.configurationService;

		if (ownerId == 0)
			ownerId = source.getJDA().getApplicationInfo().complete().getOwner().getIdLong();

		source.getJDA().getApplicationInfo().queue(owner -> {
			StringJoiner joiner = new StringJoiner("\n");
			String title = scripts.get(source, "bot.authors");
			builder.addField(title, joiner.toString(), true);

			joiner = new StringJoiner("\n");

			joiner.add(Md.a(scripts.get(source, "bot.invite_me"), DiscordUtils.getInviteUrl(alexis)));

			builder.addField("bot.info", joiner.toString(), true);

			builder.addField("bot.total_guilds", String.valueOf(jda.getGuilds().size()), true);

			Collection<User> users = jda.getUsers();
			long bots = users.stream().filter(User::isBot).count();
			String userField = String.format("%,d (%,d)", users.size() - bots, bots);
			builder.addField("bot.total_users", userField, true);

			Guild guild = jda.getGuildById(configurationService.getDiscord().getSupportGuild());
			guild.getInvites().queue(invites -> {
				Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
				invite.ifPresent(o -> builder.addField("bot.on_discord", Md.a("Elypia", o.getUrl()), true));
				event.send(builder);
			});
		});
	}

	@Static
	@Command(name = "bot.say.name", aliases = "say", help = "bot.say.help")
	public String say(CommandlerEvent<GenericMessageEvent, Message> event, @Param(name = "body", help = "The text to say.") String body) {
		GenericMessageEvent source = event.getSource();

		if (source instanceof MessageReceivedEvent)
			((MessageReceivedEvent)source).getMessage().delete().queue();
		else if (source instanceof MessageUpdateEvent)
			((MessageUpdateEvent)source).getMessage().delete().queue();

		return body;
	}

	@Static
	@Command(name = "bot.invites.name", aliases = {"invite", "invites"}, help = "bot.invites.help")
	public EmbedBuilder invites(@Channels(ChannelType.TEXT) CommandlerEvent<MessageReceivedEvent> event) {
		Guild guild = event.getSource().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getTimeCreated().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = DiscordUtils.newEmbed(guild);
		builder.setThumbnail(guild.getIconUrl() != null ? guild.getIconUrl() : guild.getSelfMember().getUser().getAvatarUrl());
		users.forEach(o -> builder.addField(o.getName(), Md.a("Invite link!", DiscordUtils.getInviteUrl(o)), true));

		return builder;
	}
}
