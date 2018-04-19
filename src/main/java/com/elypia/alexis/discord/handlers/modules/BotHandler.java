package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.annotations.access.Scope;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import org.json.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.elypia.alexis.utils.BotUtils.inviteUrl;

@Module(
	aliases = {"Bot", "Robot"},
	help = "Bot commands for stats or information."
)
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
	@Command(aliases = "ping", help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!")
	public void ping(MessageEvent event) {
		pingPong(event, "pong!");
	}

	@Static
	@Command(aliases = "pong")
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

	@Command(aliases = {"stats", "info"}, help = "Display stats on Alexis!")
	public EmbedBuilder displayStats(MessageEvent event) {
		JDA jda = event.getMessageEvent().getJDA();
		User alexis = jda.getSelfUser();
        JSONArray devs = Alexis.config.getJSONObject("discord").getJSONArray("developers");

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(alexis.getName());
		builder.setThumbnail(alexis.getAvatarUrl());

		StringJoiner joiner = new StringJoiner("\n");

		for (int i = 0; i < devs.length(); i++) {
            JSONObject object = devs.getJSONObject(i);
            User user = jda.getUserById(object.getLong("id"));
            joiner.add(Markdown.a(user.getName(), object.getString("url")));
        }

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
	@Command(aliases = "say", help = "Have Alexis repeat something you say!")
	@Param(name = "body", help = "Text Alexis should repeat!")
	public void say(MessageEvent event, String body) {
		event.tryDeleteMessage();
		event.reply(body);
	}

	@Command(aliases = "invites", help = "Get invites for all the bots in here!")
	@Scope(ChannelType.TEXT)
	public EmbedBuilder invites(MessageEvent event) {
		Guild guild = event.getMessageEvent().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getCreationTime().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = new EmbedBuilder();
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", inviteUrl(o)), true));

		return builder;
	}
}
