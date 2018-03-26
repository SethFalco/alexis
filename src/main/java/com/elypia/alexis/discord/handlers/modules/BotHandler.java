package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.Config;
import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.annotations.Scope;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.elypia.alexis.utils.BotUtils.inviteUrl;

@Module (
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

	@Command(aliases = "ping", help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!")
	public void ping(MessageEvent event) {
		pingPong(event, "pong!");
	}

	@Command(aliases = "pong")
	public void pong(MessageEvent event) {
		pingPong(event, "ping!");
	}

	private void pingPong(MessageEvent event, String text) {
		long startTime = System.currentTimeMillis();

		event.getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;

			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}

	@Command(aliases = {"stats", "info"}, help = "Display stats on Alexis!")
	public void displayStats(MessageEvent event) {
		JDA jda = event.getJDA();
		User alexis = jda.getSelfUser();
		Map<Long, String> developers = Config.developers;

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(alexis.getName());
		builder.setThumbnail(alexis.getAvatarUrl());

		StringJoiner joiner = new StringJoiner("\n");

		developers.forEach((id, url) -> {
			User user = jda.getUserById(id);
			joiner.add(Markdown.a(user.getName(), url));
		});

		builder.addField("Developer(s)", joiner.toString(), true);
		builder.addField("Scribble Master", "Jossu", true);

		builder.addField("Guilds", String.valueOf(jda.getGuilds().size()), true);

		Collection<User> users = jda.getUsers();
		long bots = users.stream().filter(User::isBot).count();
		String userField = String.format("%,d (%,d)", users.size() - bots, bots);
		builder.addField("Users (Bots)", userField, true);

		builder.addField("Guild", Markdown.a("Elypia", "https://discord.gg/"), true);
		builder.setFooter("Did you know you can support the project through the 'Amazon' module!", null);

		event.reply(builder);
	}

	@Command(aliases = "say", help = "Have Alexis repeat something you say!")
	@Parameter (name = "body", help = "Text Alexis should repeat!")
	public void say(MessageEvent event, String body) {
		event.tryDeleteMessage();
		event.reply(body);
	}

	@Command(aliases = "invites", help = "Get invites for all the bots in here!")
	@Scope(ChannelType.TEXT)
	public void invites(MessageEvent event) {
		Guild guild = event.getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getCreationTime().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = new EmbedBuilder();
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", inviteUrl(o)), true));
		event.reply(builder);
	}
}
