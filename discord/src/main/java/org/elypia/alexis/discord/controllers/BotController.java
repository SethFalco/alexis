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
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.configuration.AuthorConfig;
import org.elypia.alexis.discord.models.BotInfoModel;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.jboss.weld.context.bound.BoundRequestContext;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController(isStatic = true)
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

	private final AlexisMessages messages;
	private final AuthorConfig authorConfig;
	private final MessageSender sender;

	@Inject
	public BotController(AlexisMessages messages, AuthorConfig authorConfig, MessageSender sender) {
		this.messages = messages;
		this.authorConfig = authorConfig;
		this.sender = sender;
	}

	@StandardCommand
	public String ping() {
		return messages.pong();
	}

	@Command(hidden = true)
	@StandardCommand
	public String pong() {
		return messages.ping();
	}

	/**
	 * @param message The message from the request context.
	 */
	@StandardCommand(isDefault = true)
	public void getBotInfo(Message message) {
		JDA jda = message.getJDA();
		User self = jda.getSelfUser();
		String inviteUrl = jda.getInviteUrl();
		String avatarUrl = self.getAvatarUrl();

		BotInfoModel model = new BotInfoModel()
			.setAuthorName(authorConfig.getName())
			.setAuthorUrl(authorConfig.getUrl())
			.setAuthorLogo(authorConfig.getIconUrl())
			.setBotName(self.getName())
			.setBotInviteUrl(inviteUrl)
			.setBotAvatarUrl(avatarUrl)
			.setBotId(self.getIdLong());

		var contextCopy = AsyncUtils.copyContext();

		jda.retrieveApplicationInfo().queue((info) -> {
			var context = AsyncUtils.applyContext(contextCopy);

			model.setBotDescription(info.getDescription());
			model.setTotalGuilds(jda.getGuilds().size());

			Collection<User> users = jda.getUsers();
			model.setTotalUsers(users.size());
			long bots = users.stream().filter(User::isBot).count();
			model.setTotalBots((int)bots);

			Long supportGuildId = authorConfig.getSupportGuildId();

			if (message.isFromGuild() && message.getGuild().getIdLong() == supportGuildId)
				model.setSupportGuildText(messages.botUserInSupportGuildAlready());
			else if (supportGuildId == null)
				logger.warn("Support guild was not specified in this instance, guild info and invite will be omitted.");
			else {
				getBotInfoWithInviteLink(context, model, jda, supportGuildId);
				return;
			}

			sender.send(model);
			context.deactivate();
		});
	}

	private void getBotInfoWithInviteLink(BoundRequestContext context, BotInfoModel model, JDA jda, long supportGuildId) {
		Guild supportGuild = jda.getGuildById(supportGuildId);

		if (supportGuild == null) {
			logger.warn("The guild specified in discord.support-guild-id was not found, not displaying guild info.");
			sender.send(model);
			context.deactivate();
		} else {
			Member selfMember = supportGuild.getSelfMember();

			if (selfMember.hasPermission(Permission.MANAGE_SERVER)) {
				supportGuild.retrieveInvites().queue((invites) -> {
					addInviteLinkToEmbed(context, model, invites);
				});
			} else {
				logger.warn("The bot doesn't have the permission to get all server invites.");

				List<GuildChannel> channels = supportGuild.getChannels()
					.stream()
					.filter((channel) -> selfMember.hasPermission(channel, Permission.MANAGE_CHANNEL))
					.collect(Collectors.toList());

				if (!channels.isEmpty()) {
					// We don't want to spam the API, so we'll only check the most visible channel.
					GuildChannel channel = channels.stream().max(Comparator.comparingInt((x) -> x.getMembers().size())).get();
					channel.retrieveInvites().queue((invites) -> {
						addInviteLinkToEmbed(context, model, invites);
					});
				} else {
					logger.warn("The bot doesn't have the permission in any of the channels to fetch invite links.");

					List<GuildChannel> channels2 = supportGuild.getChannels()
						.stream()
						.filter((channel) -> selfMember.hasPermission(channel, Permission.CREATE_INSTANT_INVITE))
						.collect(Collectors.toList());

					if (!channels2.isEmpty()) {
						GuildChannel channel = channels2.stream().max(Comparator.comparingInt((x) -> x.getMembers().size())).get();
						channel.createInvite().queue((invite) -> {
							List<Invite> invites = List.of(invite);
							addInviteLinkToEmbed(context, model, invites);
						});
					} else {
						logger.warn("The bot doesn't have the permission in any of the channels to create invite links.");
						sender.send(model);
						context.deactivate();
					}
				}
			}
		}
	}

	public void addInviteLinkToEmbed(BoundRequestContext context, BotInfoModel model, List<Invite> invites) {
		Optional<Invite> optInvite = invites.stream().max(Comparator.comparingInt(Invite::getUses));

		if (optInvite.isEmpty())
			logger.warn("There are no invite links in the guild or channels, omitting guild info until invite exists.");
		else {
			Invite invite = optInvite.get();
			Invite.Guild guild = invite.getGuild();

			if (guild == null)
				logger.warn("This shouldn't be possible, but the invite created for the support guild, is somehow not associated with a guild.");
			else
				model.setSupportGuildText(MarkdownUtil.maskedLink(invite.getGuild().getName(), invite.getUrl()));
		}

		sender.send(model);
		context.deactivate();
	}

	@StandardCommand
	public String say(@Everyone Message message, @Param @NotBlank String body) {
		if (message.isFromGuild()) {
			Member self = message.getGuild().getSelfMember();

			if (self.hasPermission(message.getTextChannel(), Permission.MESSAGE_MANAGE))
				message.delete().queue();
		}

		return body;
	}

	@StandardCommand
	public EmbedBuilder invites(@Channels(ChannelType.TEXT) Message message) {
		Guild guild = message.getGuild();
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
