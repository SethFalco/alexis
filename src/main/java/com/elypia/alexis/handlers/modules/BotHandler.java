package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Module(name = "Bot Module", aliases = {"bot", "robot"}, description = "Bot commands for stats or information.")
public class BotHandler extends CommandHandler {

	private final OffsetDateTime BOT_TIME;

	public BotHandler() {
		BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));
	}

	@Override
	public boolean test() {
		return false;
	}

	@Static
	@Command(name = "Ping!", aliases = "ping", help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!")
	public void ping(MessageEvent event) {
		pingPong(event, "pong!");
	}

	@Static
	@Command(name = "Pong!", aliases = "pong")
	public void pong(MessageEvent event) {
		pingPong(event, "ping!");
	}

	private void pingPong(MessageEvent event, String text) {
		long startTime = System.currentTimeMillis();

		event.getMessageEvent().getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;
			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}

	@Command(name = "Bot Statistics", aliases = {"stats", "info"}, help = "Display stats on Alexis!")
	public EmbedBuilder displayStats(MessageEvent event) {
		JDA jda = event.getMessageEvent().getJDA();
		User alexis = jda.getSelfUser();
//		Developer[] devs = ChatbotSettings.getDiscordSettings().getDevelopers();

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(alexis.getName());
		builder.setThumbnail(alexis.getAvatarUrl());

		StringJoiner joiner = new StringJoiner("\n");

//		for (Developer dev : devs) {
//            User user = jda.getUserById(dev.getId());
//            joiner.add(Markdown.a(user.getName(), dev.getUrl()));
//        }

		builder.addField("Developer(s)", joiner.toString(), true);
		builder.addField("Scribble Master", "Jossu", true);

		builder.addField("Guilds", String.valueOf(jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size() - bots, bots);
		builder.addField("Users (Bots)", userField, true);

		builder.addField("Guild", Markdown.a("Elypia", "https://discord.gg/"), true);
		builder.setFooter("Did you know you can support the project through the 'Amazon' module!", null);

		return builder;
	}

	@Static
	@Command(name = "Say", aliases = "say", help = "Have Alexis repeat something you say!")
	@Param(name = "body", help = "Text Alexis should repeat!")
	public String say(MessageEvent event, String body) {
		event.tryDeleteMessage();
		return body;
	}

	@Static
	@Scope(ChannelType.TEXT)
	@Command(name = "Bot Invites", aliases = "invites", help = "Get invites for all the bots in here!")
	public EmbedBuilder invites(MessageEvent event) {
		Guild guild = event.getMessageEvent().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getCreationTime().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = new EmbedBuilder();
		builder.setThumbnail(guild.getIconUrl());
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", BotUtils.formInviteUrl(o)), true));

		return builder;
	}
}
