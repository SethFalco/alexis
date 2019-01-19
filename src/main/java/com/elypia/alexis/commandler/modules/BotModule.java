package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.BotConfig;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.elyscript.ElyScript;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Module(id = "Bot", aliases = {"bot", "robot"}, help = "bot.help")
public class BotModule extends JDACHandler {

	private static final Logger logger = LoggerFactory.getLogger(BotModule.class);

	/**
	 * After this time bots had the same client id as user id which allows
	 * us to generate invite links for them, bots before this time however
	 * didn't have matching client/user ids and so their links would appear.
	 * dead. We have this here to make sure we only generate links for bots
	 * made after this time.
	 */
	private static final OffsetDateTime BOT_TIME = OffsetDateTime.of(2016, 7, 19, 1, 52, 0, 0, ZoneOffset.ofHours(0));

	private long ownerId;

	/**
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link ModuleData} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 */
	public BotModule(Commandler<GenericMessageEvent, Message> commandler) {
		super(commandler);
	}

	@Example(command = ">ping", response = "pong!")
	@Static
	@Command(id = "bot.ping.name", aliases = "ping", help = "bot.ping.help")
	public String ping(JDACEvent event) {
		return scripts.get("bot.pong.name");
	}

	@Static
	@Command(id = "bot.pong.name", aliases = "pong")
	public String pong(JDACEvent event) {
		return scripts.get("bot.ping.name");
	}

	@Default
	@Command(id = "bot.info.name", aliases = {"stats", "info"}, help = "bot.info.help")
	public void displayStats(JDACEvent event) {
		GenericMessageEvent source = event.getSource();
		EmbedBuilder builder = BotUtils.newEmbed(source.getGuild());
		JDA jda = source.getJDA();
		User alexis = jda.getSelfUser();

		builder.setTitle(alexis.getName());
		builder.setDescription(scripts.get(source, "bot.description") + "\n_ _");
		builder.setThumbnail(alexis.getAvatarUrl());
		BotConfig config = Alexis.config;

		if (ownerId == 0)
			ownerId = source.getJDA().getApplicationInfo().complete().getOwner().getIdLong();

		source.getJDA().getApplicationInfo().queue(owner -> {
			StringJoiner joiner = new StringJoiner("\n");
			String title = scripts.get(source, "bot.authors");
			builder.addField(title, joiner.toString(), true);

			joiner = new StringJoiner("\n");

			joiner.add(Md.a(scripts.get(source, "bot.invite_me"), BotUtils.getInviteUrl(alexis)));

			builder.addField("bot.info", joiner.toString(), true);

			builder.addField("bot.total_guilds", String.valueOf(jda.getGuilds().size()), true);

			Collection<User> users = jda.getUsers();
			long bots = users.stream().filter(User::isBot).count();
			String userField = String.format("%,d (%,d)", users.size() - bots, bots);
			builder.addField("bot.total_users", userField, true);

			Guild guild = jda.getGuildById(config.getDiscordConfig().getSupportGuild());
			guild.getInvites().queue(invites -> {
				Optional<Invite> invite = invites.stream().max((one, two) -> one.getUses() > two.getUses() ? 1 : -1);
				invite.ifPresent(o -> builder.addField("bot.support_guild", Md.a("Elypia", o.getUrl()), true));
				event.send(builder);
			});
		});
	}

	@Static
	@Command(id = "bot.script.name", aliases = "script", help = "bot.script.help")
	@Param(id = "common.body", help = "bot.param.body.script.help")
	public String sayEly(JDACEvent event, @Everyone String body) {
		event.delete();
		return new ElyScript(body).compile();
	}

	@Static
	@Command(id = "bot.say.name", aliases = "say", help = "bot.say.help")
	@Param(id = "common.body", help = "bot.param.body.say.help")
	public String say(JDACEvent event, @Everyone String body) {
		event.delete();
		return body;
	}

	@Static
	@Command(id = "bot.invites.name", aliases = {"invite", "invites"}, help = "bot.invites.help")
	public EmbedBuilder invites(@Channels(ChannelType.TEXT) JDACEvent event) {
		Guild guild = event.getSource().getGuild();
		Collection<Member> bots = guild.getMembers();

		Collection<User> users = bots.stream().map(Member::getUser).filter(User::isBot).filter(o -> {
			return o.getTimeCreated().isAfter(BOT_TIME);
		}).collect(Collectors.toList());

		EmbedBuilder builder = BotUtils.newEmbed(guild);
		builder.setThumbnail(guild.getIconUrl() != null ? guild.getIconUrl() : guild.getSelfMember().getUser().getAvatarUrl());
		users.forEach(o -> builder.addField(o.getName(), Md.a("Invite link!", BotUtils.getInviteUrl(o)), true));

		return builder;
	}
}
