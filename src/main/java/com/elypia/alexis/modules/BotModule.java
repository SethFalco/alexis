package com.elypia.alexis.modules;

import com.elypia.alexis.ChatBot;
import com.elypia.alexis.services.ConfigurationService;
import com.elypia.alexis.utils.*;
import com.elypia.cmdlrdiscord.constraints.Channels;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.google.inject.Inject;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import org.slf4j.*;

import javax.inject.Singleton;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Module(name = "bot", aliases = {"bot", "robot"})
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
