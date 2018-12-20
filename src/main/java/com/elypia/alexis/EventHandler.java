package com.elypia.alexis;

import com.elypia.alexis.entities.*;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.alexis.google.translate.TranslateHelper;
import com.elypia.alexis.utils.*;
import com.elypia.elypiai.runescape.RuneScape;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.*;

import java.time.OffsetDateTime;
import java.util.*;

public class EventHandler extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

	private TranslateHelper translate = new TranslateHelper();

	/**
	 * Occurs when the bot succesfully logs in.
	 *
	 * @param event ReadyEvent
	 */
	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - Alexis.START_TIME;
		logger.info("Time taken to launch: {}ms", String.format("%,d", timeElapsed));

		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 *
	 * @param event GuildJoinEvent
	 */

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();
		TextChannel channel = BotUtils.getWriteableChannel(event);

		if (guild.getSelfMember().getJoinDate().isBefore(OffsetDateTime.now().minusMinutes(10)))
			return;

		if (channel == null)
			return;

		String prefix = Alexis.config.getDiscordConfig().getPrefix();
		String message = "Thank you for inviting me! My default prefix is `" + prefix + "` but you can mention me too!\nFeel free to try my help command!";
		channel.sendMessage(message).queue();

		logger.info("The guild {} just invited me!\n{}", guild.getName(), statsMessage(event.getJDA()));

		GuildData data = new GuildData();
		data.setGuildId(guild.getIdLong());
		Alexis.getDatabaseManager().commit(data);
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Guild guild = event.getGuild();
		logger.info("The guild %s just kicked me!\n{}", guild.getName(), statsMessage(event.getJDA()));
	}

	private String statsMessage(JDA jda) {
		final String MESSAGE = "I'm now in %,d guilds, totalling %,d users, and %,d bots!";

		int guildCount = jda.getGuilds().size();

		List<User> users = jda.getUsers();
		long botCount = users.stream().filter(User::isBot).count();
		long userCount = users.size() - botCount;

		return String.format(MESSAGE, guildCount, userCount, botCount);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		onGuildMemberEvent(event, true);
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		onGuildMemberEvent(event, false);
	}

	public void onGuildMemberEvent(GenericGuildMemberEvent event, boolean join) {
		boolean bot = event.getUser().isBot();

		Guild guild = event.getGuild();
		GuildData data = Alexis.getDatabaseManager().query(GuildData.class, "guild_id", guild.getIdLong());

		GreetingSettings greetings = data.getSettings().getGreetingSettings();
		GreetingSetting greeting = join ? greetings.getJoin() : greetings.getLeave();
		MessageSettings settings = bot ? greeting.getBot() : greeting.getUser();

		if (settings.isEnabled()) {
			JDA jda = event.getJDA();
			TextChannel channel = jda.getTextChannelById(settings.getChannel());
			String message = settings.getMessage();
//			message = BotUtils.get(event, message, event);

			channel.sendMessage(message).queue();
		}
	}

	@Override
	public void onGuildBan(GuildBanEvent event) {

	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {

	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {

	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getChannelType().isGuild() && !event.getAuthor().isBot())
			xp(event);
	}

	private void xp(MessageReceivedEvent event) {
		if (event.getGuild().getMembers().stream().filter(o -> !o.getUser().isBot()).count() == 1)
			return;

		long userId = event.getAuthor().getIdLong();
		UserData userData = UserData.query(userId);

		if (!userData.isEligibleForXp(event)) {
			userData.setLastMessage(new Date());
			userData.commit();
			return;
		}

		userData.setLastMessage(new Date());

		long guildId = event.getGuild().getIdLong();
		GuildData guildData = GuildData.query(guildId);
		GuildSettings settings = guildData.getSettings();

		MemberData memberData = MemberData.query(userId, guildId);

		int reward = userData.calculateXp(event);

		userData.incrementXp(reward);
		userData.commit();

		guildData.incrementXp(reward);
		guildData.commit();

		int currentLevel = RuneScape.parseXpAsLevel(memberData.getXp());
		int newLevel = RuneScape.parseXpAsLevel(memberData.incrementXp(reward));

		if (currentLevel != newLevel) {
			boolean enabled = settings.getLevelSettings().getNotifySettings().isEnabled();

			if (enabled)
				event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
		}

		List<SkillEntry> skills = settings.getSkills();
		List<MemberSkill> memberSkills = memberData.getSkills();

		skills.forEach((skill) -> {
			if (skill.getChannels().contains(event.getChannel().getIdLong())) {
				if (memberSkills.stream().noneMatch(o -> o.getName().equalsIgnoreCase(skill.getName())))
					memberSkills.add(new MemberSkill(skill.getName()));

				for (MemberSkill mSkill : memberSkills) {
					if (mSkill.getName().equalsIgnoreCase(skill.getName())) {
						int skillLevel = RuneScape.parseXpAsLevel(mSkill.getXp());
						int newSkillLevel = RuneScape.parseXpAsLevel(mSkill.incrementXp(reward));

						if (skillLevel != newSkillLevel && skill.isNotify()) {
							var params = Map.of("skill", skill.getName(), "level", newSkillLevel);
//							event.getChannel().sendMessage(BotUtils.get("skill.level_up", event, params)).queue();
						}

						return;
					}
				}
			}
		});

		memberData.commit();
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		boolean isBot = event.getUser().isBot();
		boolean isGuild = event.getChannelType().isGuild();

		if (isBot || !isGuild)
			return;

		Guild guild = event.getGuild();
		long id = guild.getIdLong();
		GuildData data = GuildData.query(id);
		TranslateSettings settings = data.getSettings().getTranslateSettings();

		if (settings.isEnabled())
			translate(event, settings);
	}

	/**
	 * If the translate feature is enabled for the Guild we should check
	 * reactions for if they're flags and if so, respond to the message
	 * by translating the message to an appropriate languages the flag
	 * may represent. <br>
	 * <br>
	 * <strong>Note:</strong> We pass the settings over so we don't have
	 * to query them again.
	 *
	 * @param event The source event which may have request translating.
	 * @param settings The guild settings regarding translation.
	 */
	private void translate(MessageReactionAddEvent event, TranslateSettings settings) {
		if (!settings.isEnabled())
			return;

		String code = event.getReactionEmote().getName();
		var languages = translate.getSupportedLangauges();

		for (var entry : languages.entrySet()) {
			Country[] countries = entry.getKey().getCountries();
			Language value = entry.getValue();

			for (Country country : countries) {
				if (country.getUnicodeEmote().equals(code)) {
					event.getChannel().getMessageById(event.getMessageIdLong()).queue(message -> {
						String content = message.getContentStripped();
						Translation translation = translate.translate(content, value);
						var source = translate.getLanguage(translation.getSourceLanguage()).getKey();

						EmbedBuilder builder = new EmbedBuilder();
						builder.addField("Source (" + source.getName() + ")", content + "\n_ _", false);
						builder.addField("Target (" + value.getName() + ")", translation.getTranslatedText(), false);
						builder.setImage("https://elypia.com/resources/google.png");
						builder.setFooter("http://translate.google.com/", null);

						if (event.getGuild() != null)
							builder.setColor(event.getGuild().getSelfMember().getColor());

						if (!settings.isPrivate())
							event.getChannel().sendMessage(builder.build()).queue();
						else {
							event.getMember().getUser().openPrivateChannel().queue(o -> {
								o.sendMessage(builder.build()).queue();
							});
						}
					});

					return;
				}
			}
		}
	}
}
