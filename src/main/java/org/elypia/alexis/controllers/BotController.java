/*
 * Alexis - A general purpose chatbot for Discord.
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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.*;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.inject.*;
import javax.validation.constraints.NotBlank;
import java.time.*;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class BotController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(BotController.class);

	/**
	 * After this time bots had the same client id as user id which allows
	 * us to generate invite links for them, bots before this time however
	 * didn't have matching client/user ids and so their links would appear.
	 * dead. We have this here to make sure we only generate links for bots
	 * made after this time.
	 */
	private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

	private final DiscordConfig discordConfig;

	@Inject
	public BotController(final DiscordConfig discordConfig) {
		this.discordConfig = discordConfig;
	}

	public String ping() {
		return "pong!";
	}

	public String pong() {
		return "ping!";
	}

	public void info(ActionEvent<Event, Message> event) {
		Event source = event.getRequest().getSource();
		EmbedBuilder builder = DiscordUtils.newEmbed(event);
		JDA jda = source.getJDA();
		User self = jda.getSelfUser();

		builder.setTitle(self.getName());
		builder.setDescription("I'm a cool bot." + "\n_ _");
		builder.setThumbnail(self.getAvatarUrl());

		source.getJDA().retrieveApplicationInfo().queue((info) -> {
//			StringJoiner joiner = new StringJoiner("\n");
//			String title = "Authors";
//			builder.addField(title, joiner.toString(), true);
//
//			joiner = new StringJoiner("\n");
//
//			joiner.add(Md.a(scripts.get(source, "bot.invite_me"), DiscordUtils.getInviteUrl(self)));
//
//			builder.addField("bot.info", joiner.toString(), true);
//			builder.addField("bot.total_guilds", String.valueOf(jda.getGuilds().size()), true);
//
//			Collection<User> users = jda.getUsers();
//			long bots = users.stream().filter(User::isBot).count();
//			String userField = String.format("%,d (%,d)", users.size() - bots, bots);
//			builder.addField("bot.total_users", userField, true);
//
//			Guild guild = jda.getGuildById(configurationService.getDiscord().getSupportGuild());
//			guild.getInvites().queue(invites -> {
//				Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
//				invite.ifPresent(o -> builder.addField("bot.on_discord", Md.a("Elypia", o.getUrl()), true));
//				event.send(builder);
//			});
		});
	}

	public String say(@Everyone ActionEvent<Event, Message> event, @NotBlank String body) {
		Event source = event.getRequest().getSource();
		Guild guild = EventUtils.getGuild(source);

		if (source != null) {
			TextChannel channel = EventUtils.getTextChannel(source);
			Member self = guild.getSelfMember();

			if (self.hasPermission(channel, Permission.MESSAGE_MANAGE))
				EventUtils.getMessage(source).delete().queue();
		}

		return body;
	}

	public EmbedBuilder invites(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
		Guild guild = EventUtils.getGuild(event.getRequest().getSource());
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getTimeCreated().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = DiscordUtils.newEmbed(guild);
		builder.setThumbnail(guild.getIconUrl() != null ? guild.getIconUrl() : guild.getSelfMember().getUser().getAvatarUrl());
		users.forEach(o -> builder.addField(o.getName(), "[Invite link!](" + DiscordUtils.getInviteUrl(o) + ")", true));

		return builder;
	}
}
