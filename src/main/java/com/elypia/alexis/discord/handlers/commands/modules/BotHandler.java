package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.annotations.Scope;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
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

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(String.format("%s stats!", alexis.getName()));
		builder.setThumbnail(alexis.getAvatarUrl());
		builder.addField("Guilds", String.valueOf(jda.getGuilds().size()), false);
		builder.addField("Users", String.valueOf(jda.getUsers().size()), false);

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
		users.forEach(o -> builder.addField(o.getName(), Markdown.a("Invite link!", inviteUrl(o)), false));
		event.reply(builder);
	}
}
