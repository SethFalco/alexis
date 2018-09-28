package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.*;
import com.elypia.alexis.config.embedded.Author;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import com.elypia.commandler.jda.annotations.validation.param.Everyone;
import com.elypia.elypiai.utils.Markdown;
import com.elypia.elyscript.ElyScript;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Module(name = "bot.title", aliases = {"bot", "robot"}, help = "bot.help")
public class BotHandler extends JDAHandler {

	private static final Logger logger = LoggerFactory.getLogger(BotHandler.class);

	/**
	 * After this time bots had the same client id as user id which allows
	 * us to generate invite links for them, bots before this time however
	 * didn't have matching client/user ids and so their links would appear.
	 * dead. We have this here to make sure we only generate links for bots
	 * made after this time.
	 */
	private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

	@Static
	@Command(name = "bot.ping.name", aliases = "ping", help = "bot.ping.help")
	public void ping(JDACommand event) {
		event.replyScript("bot.pong.name");
	}

	@Static
	@Command(name = "bot.pong.name", aliases = "pong")
	public void pong(JDACommand event) {
		event.replyScript("bot.ping.name");
	}

	@Default
	@Command(name = "bot.info.name", aliases = {"stats", "info"}, help = "bot.info.help")
	public void displayStats(JDACommand event) {
		GenericMessageEvent source = event.getSource();
		EmbedBuilder builder = BotUtils.createEmbedBuilder(source.getGuild());
		JDA jda = source.getJDA();
		User alexis = jda.getSelfUser();

		builder.setTitle(alexis.getName());
		builder.setDescription(BotUtils.getScript("bot.description", source) + "\n_ _");
		builder.setThumbnail(alexis.getAvatarUrl());

		BotConfig config = Alexis.config;
		List<Author> authors = config.getDiscordConfig().getAuthors();
		StringJoiner joiner = new StringJoiner("\n");

		for (Author author : authors) {
			long id = author.getId();
			User user = jda.getUserById(id);

			if (user != null)
				joiner.add(Markdown.a(user.getName(), author.getUrl()) + " - " + author.getRole());
			else
				logger.warn("The developer for id {} couldn't be found.", id);
		}

		Map<String, Object> authorParams = new HashMap<>();
		authorParams.put("authors", authors.size());

		String title = BotUtils.getScript("bot.authors", source, authorParams);
		builder.addField(title, joiner.toString(), true);

		joiner = new StringJoiner("\n");

		joiner.add(Markdown.a(BotUtils.getScript("bot.invite_me", source), BotUtils.getInviteUrl(alexis)));

		builder.addField("bot.info", joiner.toString(), true);

		builder.addField("bot.total_guilds", String.valueOf(jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size() - bots, bots);
		builder.addField("bot.total_users", userField, true);

		Guild guild = jda.getGuildById(config.getDiscordConfig().getSupportGuild());
		guild.getInvites().queue(invites -> {
			Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
			invite.ifPresent(o -> builder.addField("bot.support_guild", Markdown.a("Elypia", o.getURL()), true));
			event.reply(builder);
		});
	}

	@Static
	@Command(name = "bot.script.name", aliases = "script", help = "bot.script.help")
	@Param(name = "common.body", help = "bot.param.body.script.help")
	public String sayEly(JDACommand event, @Everyone String body) {
		event.deleteMessage();
		return new ElyScript(body).compile();
	}

	@Static
	@Command(name = "bot.say.name", aliases = "say", help = "bot.say.help")
	@Param(name = "common.body", help = "bot.param.body.say.help")
	public String say(JDACommand event, @Everyone String body) {
		event.deleteMessage();
		return body;
	}

	@Static
	@Channel(ChannelType.TEXT)
	@Command(name = "bot.invites.name", aliases = {"invite", "invites"}, help = "bot.invites.help")
	public EmbedBuilder invites(JDACommand event) {
		Guild guild = event.getSource().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getCreationTime().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = BotUtils.createEmbedBuilder(guild);
		builder.setThumbnail(guild.getIconUrl() != null ? guild.getIconUrl() : guild.getSelfMember().getUser().getAvatarUrl());
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", BotUtils.getInviteUrl(o)), true));

		return builder;
	}
}
