package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.*;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.annotations.validation.param.Everyone;
import com.elypia.elypiai.utils.Markdown;
import com.elypia.elyscript.ElyScript;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Module(name = "Bot", aliases = {"bot", "robot"}, help = "help.bot")
public class BotHandler extends JDAHandler {

	private final OffsetDateTime BOT_TIME;

	public BotHandler() {
		BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));
	}

	@Static
	@Command(name = "Ping!", aliases = "ping", help = "help.bot.ping")
	public void ping(JDACommand event) {
		pingPong(event, "pong!");
	}

	@Static
	@Command(name = "Pong!", aliases = "pong")
	public void pong(JDACommand event) {
		pingPong(event, "ping!");
	}

	private void pingPong(JDACommand event, String text) {
		long startTime = System.currentTimeMillis();

		event.getSource().getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;
			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}

	@Default
	@Command(name = "Bot Stats", aliases = {"stats", "info"}, help = "help.bot.info")
	public void displayStats(JDACommand event) {
		GenericMessageEvent source = event.getSource();
		EmbedBuilder builder = BotUtils.createEmbedBuilder(source.getGuild());
		JDA jda = source.getJDA();
		User alexis = jda.getSelfUser();

		builder.setTitle(alexis.getName());
		builder.setDescription(BotUtils.getScript("bot.description", source) + "\n_ _");
		builder.setThumbnail(alexis.getAvatarUrl());

		AlexisConfig config = Alexis.getConfig();
		List<Author> authors = config.getDiscordConfig().getAuthors();
		StringJoiner joiner = new StringJoiner("\n");

		for (Author author : authors) {
			long id = author.getId();
			User user = jda.getUserById(id);

			if (user != null)
				joiner.add(Markdown.a(user.getName(), author.getUrl()) + " - " + author.getRole());
			else
				BotLogger.log(event, Level.INFO, "The developer for id %d couldn't be found.", id);
		}

		Map<String, Object> authorParams = new HashMap<>();
		authorParams.put("total_authors", authors.size());

		String title = BotUtils.getScript("bot.authors.title", source, authorParams);
		builder.addField(title, joiner.toString(), true);

		joiner = new StringJoiner("\n");

		joiner.add("Language - Java");
		joiner.add("Library - " + Markdown.a("JDA", "https://github.com/DV8FromTheWorld/JDA"));
		joiner.add("Invite - " + Markdown.a(BotUtils.getScript("bot.invite_me.value", source), BotUtils.formInviteUrl(alexis)));

		builder.addField("Info", joiner.toString(), true);

		builder.addField("Total Guilds", String.valueOf(jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size() - bots, bots);
		builder.addField("Total Users (Bots)", userField, true);

		Map<String, Object> supportParams = new HashMap<>();
		supportParams.put("prefix", commandler.getConfiler().getPrefixes(commandler, source)[0]);

		builder.setFooter(BotUtils.getScript("bot.support", source, supportParams), null);

		Guild guild = jda.getGuildById(config.getDiscordConfig().getSupportGuild());
		guild.getInvites().queue(invites -> {
			Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
			invite.ifPresent(o -> builder.addField("Support Guild", Markdown.a("Elypia", o.getURL()), true));
			event.reply(builder);
		});
	}

	@Static
	@Command(name = "Say", aliases = "say", help = "help.bot.say")
	@Param(name = "body", help = "help.bot.say.body")
	public String say(JDACommand event, @Everyone String body) {
		event.deleteMessage();
		return body;
	}

	@Static
	@Scope(ChannelType.TEXT)
	@Command(name = "Bot Invites", aliases = {"invite", "invites"}, help = "help.bot.invites")
	public EmbedBuilder invites(JDACommand event) {
		Guild guild = event.getSource().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getCreationTime().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = BotUtils.createEmbedBuilder(guild);
		builder.setThumbnail(guild.getIconUrl() != null ? guild.getIconUrl() : guild.getSelfMember().getUser().getAvatarUrl());
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", BotUtils.formInviteUrl(o)), true));

		return builder;
	}

	@Static
	@Command(name = "ElyScript", aliases = "script", help = "help.bot.script")
	@Param(name = "body", help = "help.bot.script.body")
	public String sayEly(JDACommand event, @Everyone String body) {
		event.deleteMessage();
		return new ElyScript(body).compile();
	}
}
