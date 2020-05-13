/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.*;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
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

	private static final String AUTHOR_VALUE = MarkdownUtil.maskedLink("Elypia", "https://elypia.org/");

	private final DiscordConfig config;
	private final AlexisMessages messages;

	@Inject
	public BotController(final DiscordConfig config, final AlexisMessages messages) {
		this.config = config;
		this.messages = messages;
	}

	public String ping() {
		return messages.pong();
	}

	public String pong() {
		return messages.ping();
	}

	public MessageEmbed info(ActionEvent<Event, Message> event) {
		Event source = event.getRequest().getSource();
		EmbedBuilder builder = DiscordUtils.newEmbed(event);
		JDA jda = source.getJDA();
		User self = jda.getSelfUser();

		builder.setTitle(self.getName());
		builder.setThumbnail(self.getAvatarUrl());

		ApplicationInfo info = source.getJDA().retrieveApplicationInfo().complete();
		builder.setDescription(info.getDescription());

		builder.addField(messages.botAuthor(), AUTHOR_VALUE, true);
		builder.addField(messages.botTotalGuilds(), String.format("%,d", jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size(), bots);
		builder.addField(messages.botTotalUsers(), userField, true);

		Long guildId = config.getSupportGuildId();

		if (guildId == null) {
			logger.warn("Support guild was not specified in this instance, guild info and invite will be omitted.");
			return builder.build();
		}

		Guild guild = jda.getGuildById(config.getSupportGuildId());

		if (guild == null) {
			logger.warn("The guild specified in discord.support-guild-id was not found, not displaying guild info.");
			return builder.build();
		}

		Member selfMember = guild.getSelfMember();
		List<Invite> invites = null;

		if (selfMember.hasPermission(Permission.MANAGE_SERVER)) {
			invites = guild.retrieveInvites().complete();
		} else {
			logger.warn("The bot doesn't have the permission to get all server invites.");

			List<GuildChannel> channels = guild.getChannels()
				.stream()
				.filter((channel) -> selfMember.hasPermission(channel, Permission.MANAGE_CHANNEL))
				.collect(Collectors.toList());

			if (!channels.isEmpty()) {
				GuildChannel channel = channels.stream().max(Comparator.comparingInt((x) -> x.getMembers().size())).get();
				invites = channel.retrieveInvites().complete();
			} else {
				logger.warn("The bot doesn't have the permission in any of the channels to fetch invite links.");

				List<GuildChannel> channels2 = guild.getChannels()
					.stream()
					.filter((channel) -> selfMember.hasPermission(channel, Permission.CREATE_INSTANT_INVITE))
					.collect(Collectors.toList());

				if (!channels2.isEmpty()) {
					GuildChannel channel = channels2.stream().max(Comparator.comparingInt((x) -> x.getMembers().size())).get();
					invites = List.of(channel.createInvite().complete());
				} else {
					logger.warn("The bot doesn't have the permission in any of the channels to create invite links.");
				}
			}
		}

		if (invites == null || invites.isEmpty()) {
			logger.warn("There are no invite links in the guild or channels, omitting guild info until invite exists.");
			return builder.build();
		}

		Optional<Invite> invite = invites.stream().max(Comparator.comparingInt(Invite::getUses));
		invite.ifPresent((o) -> builder.addField(messages.botSupportGuild(), MarkdownUtil.maskedLink(guild.getName(), o.getUrl()), true));

		return builder.build();
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

		Collection<User> users = bots.stream()
			.map(Member::getUser)
			.filter(User::isBot)
			.filter((o) -> o.getTimeCreated().isAfter(BOT_TIME))
			.collect(Collectors.toList());

		EmbedBuilder builder = DiscordUtils.newEmbed(guild);
		String iconUrl = guild.getIconUrl();
		builder.setThumbnail((iconUrl != null) ? iconUrl : guild.getSelfMember().getUser().getAvatarUrl());

		for (User bot : users) {
			String name = bot.getName();
			String inviteText = messages.inviteBot(name);
			String value = MarkdownUtil.maskedLink(inviteText, DiscordUtils.getInviteUrl(bot));
			builder.addField(name, value, true);
		}

		return builder;
	}
}
