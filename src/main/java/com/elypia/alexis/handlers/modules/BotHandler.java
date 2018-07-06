package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.*;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.annotations.validation.param.Everyone;
import com.elypia.commandler.events.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.Markdown;
import com.elypia.elypiai.utils.elyscript.ElyScript;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Module(name = "Bot", aliases = {"bot", "robot"}, description = "Bot commands for stats or information.")
public class BotHandler extends CommandHandler {

	private final OffsetDateTime BOT_TIME;

	public BotHandler() {
		BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));
	}

	@Static
	@Command(name = "Ping!", aliases = "ping", help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!")
	public void ping(AbstractEvent event) {
		pingPong(event, "pong!");
	}

	@Static
	@Command(name = "Pong!", aliases = "pong")
	public void pong(AbstractEvent event) {
		pingPong(event, "ping!");
	}

	private void pingPong(AbstractEvent event, String text) {
		long startTime = System.currentTimeMillis();

		event.getMessageEvent().getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;
			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}

	@Default
	@Command(name = "Bot Stats", aliases = {"stats", "info"}, help = "Get stats, info, and support information for Alexis!")
	public void displayStats(AbstractEvent event) {
		EmbedBuilder builder = BotUtils.createEmbedBuilder(event.getMessageEvent().getGuild());
		JDA jda = event.getMessageEvent().getJDA();
		User alexis = jda.getSelfUser();

		builder.setTitle(alexis.getName());
		builder.setDescription("Alexis is a multi-purpose chatbot with integration and notifiers for many services including Twitch, osu! and RuneScape. It contains fun, music, and moderation functionality.\n_ _");
		builder.setThumbnail(alexis.getAvatarUrl());

		AlexisConfig config = Alexis.getConfig();
		List<Author> authors = config.getDiscordConfig().getAuthors();
		StringJoiner joiner = new StringJoiner("\n");

		for (Author author : authors) {
			long id = author.getId();
			User user = jda.getUserById(id);

			if (user != null)
				joiner.add(Markdown.a(user.getName(), author.getUrl() + " - " + author.getRole()));
			else
				BotUtils.log(Level.WARNING, "The developer for id %d couldn't be found.", id);
		}

		String title = authors.size() > 1 ? "Authors" : "Author";
		builder.addField(title, joiner.toString(), true);

		joiner = new StringJoiner("\n");

		joiner.add("Language - Java");
		joiner.add("Library - " + Markdown.a("JDA", "https://github.com/DV8FromTheWorld/JDA"));
		joiner.add("Invite - " + Markdown.a("Invite me!", BotUtils.formInviteUrl(alexis)));

		builder.addField("Info", joiner.toString(), true);

		builder.addField("Total Guilds", String.valueOf(jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size() - bots, bots);
		builder.addField("Total Users (Bots)", userField, true);

		String prefix = commandler.getConfiler().getPrefix(event.getMessageEvent());
		builder.setFooter("You can support the project through the '" + prefix + "amazon' module!", null);

		Guild guild = jda.getGuildById(config.getDiscordConfig().getSupportGuild());
		guild.getInvites().queue(invites -> {
			Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
			invite.ifPresent(o -> builder.addField("Support Guild", Markdown.a("Elypia", o.getURL()), true));
			event.reply(builder);
		});
	}

	@Static
	@Command(name = "Say", aliases = "say", help = "Have Alexis repeat something you say!")
	@Param(name = "body", help = "Text Alexis should repeat!")
	public String say(AbstractEvent event, @Everyone String body) {
		event.tryDeleteMessage();
		return body;
	}

	@Static
	@Scope(ChannelType.TEXT)
	@Command(name = "Bot Invites", aliases = {"invite", "invites"}, help = "Get invites for all the bots in here!")
	public EmbedBuilder invites(AbstractEvent event) {
		Guild guild = event.getMessageEvent().getGuild();
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
	@Command(name = "ElyScript", aliases = "script", help = "Have Alexis repeat something you say using ElyScript!")
	@Param(name = "body", help = "Text Alexis should repeat!")
	public String sayEly(AbstractEvent event, @Everyone String body) {
		event.tryDeleteMessage();
		return new ElyScript(body).compile();
	}
}
