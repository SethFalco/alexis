package com.elypia.alexis.handlers;

import com.elypia.alexis.*;
import com.elypia.alexis.entities.*;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.alexis.google.translate.TranslateHelper;
import com.elypia.alexis.utils.*;
import com.elypia.elypiai.runescape.RuneScape;
import com.elypia.elypiai.utils.Country;
import com.google.cloud.translate.*;
import com.mongodb.MongoClient;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.voice.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.Query;

import java.time.OffsetDateTime;
import java.util.logging.Level;

public class EventHandler extends ListenerAdapter {

    private Chatbot chatbot;
    private MongoClient client;
    private Morphia morphia;
	private Datastore store;

	public EventHandler(Chatbot chatbot) {
		this.chatbot = chatbot;
		this.client = chatbot.getDatabase();
		this.morphia = chatbot.getMorphia();
		this.store = chatbot.getDatastore();
	}

	/**
	 * Occurs when the bot succesfully logs in.
	 *
	 * @param event ReadyEvent
	 */

	@Override
	public void onReady(ReadyEvent event) {
		long timeElapsed = System.currentTimeMillis() - chatbot.getStartUpTime();
		BotLogger.log(event, Level.FINER, "Time taken to launch: %,dms", timeElapsed);

		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 *
	 * @param event GuildJoinEvent
	 */

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		// ? Check if we actually joined the guild now or did Discord just dish out an extra event again. ^-^'
		if (event.getGuild().getSelfMember().getJoinDate().isBefore(OffsetDateTime.now().minusMinutes(10)))
			return;

		Guild guild = event.getGuild();
		TextChannel channel = BotUtils.getWriteableChannel(event);

		if (channel != null) {
			String prefix = Alexis.getConfig().getDiscordConfig().getPrefix();
			String message = "Thank you for inviting me! My default prefix is `" + prefix + "` but you can mention me too!\nFeel free to try my help command!";
			channel.sendMessage(message).queue();
		}

		BotLogger.log(event, Level.INFO, "The guild  %s just invited me!", guild.getName());
		BotLogger.log(event.getJDA());

		GuildData data = new GuildData();
		data.setGuildId(guild.getIdLong());
		store.save(data);
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Guild guild = event.getGuild();

		BotLogger.log(event, Level.INFO, "The guild %s just kicked me!", guild.getName());
		BotLogger.log(event.getJDA());
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
		Query<GuildData> query = store.createQuery(GuildData.class);
		GuildData data = query.filter("guild_id ==", guild.getIdLong()).get();

		GreetingSettings greetings = data.getSettings().getGreetingSettings();
		GreetingSetting greeting = join ? greetings.getWelcome() : greetings.getFarewell();
		MessageSettings settings = bot ? greeting.getBot() : greeting.getUser();

		if (settings.isEnabled()) {
			JDA jda = event.getJDA();
			TextChannel channel = jda.getTextChannelById(settings.getChannel());
			String message = settings.getMessage();
			message = BotUtils.buildScript(message, event);

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
		handleXp(event);
	}

	private void handleXp(MessageReceivedEvent event) {
		User author = event.getAuthor();

		if (!event.isFromType(ChannelType.TEXT) || author.isBot())
			return;

		if (event.getGuild().getMembers().stream().filter(o -> !o.getUser().isBot()).count() == 1)
			return;

		long userId = author.getIdLong();

		UserData userData = UserData.query(userId);
		userData.grantXp(event);
		userData.commit();

		if (!event.getChannelType().isGuild())
			return;

		long guildId = event.getGuild().getIdLong();

		GuildData guildData = GuildData.query(guildId);

		MemberData memberData = MemberData.query(userId, guildId);
		int currentLevel = RuneScape.parseXpAsLevel(memberData.getXp());

		if (memberData.grantXp(event)) {
			int newLevel = RuneScape.parseXpAsLevel(memberData.getXp());

			if (currentLevel != newLevel) {
				boolean enabled = guildData.getSettings().getLevelSettings().getNotifySettings().isEnabled();

				if (enabled) {
					event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
				}
			}
		}

		memberData.commit();
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if (event.getUser().isBot())
			return;

		handleTranslate(event);
	}

	private TranslateHelper translate = new TranslateHelper();

	private void handleTranslate(MessageReactionAddEvent event) {
		String code = event.getReactionEmote().getName();
		var languages = translate.getSupportedLangauges();

		for (var entry : languages.entrySet()) {
			Language value = entry.getValue();

			for (Country country : entry.getKey().getCountries()) {
				if (country.getUnicodeEmote().equals(code)) {
					event.getChannel().getMessageById(event.getMessageIdLong()).queue(message -> {
						String content = message.getContentStripped();
						Translation translation = translate.translate(content, value);
						var source = translate.getLanguage(translation.getSourceLanguage()).getKey();

						EmbedBuilder builder = new EmbedBuilder();
						builder.addField("Source (" + source.getLanguageName() + ")", content + "\n_ _", false);
						builder.addField("Target (" + value.getName() + ")", translation.getTranslatedText(), false);
						builder.setImage("https://cdn.discordapp.com/attachments/436154993247256586/460187735936991233/color-short2x.png");
						builder.setFooter("http://translate.google.com/", null);

						if (event.getGuild() != null)
							builder.setColor(event.getGuild().getSelfMember().getColor());

						event.getChannel().sendMessage(builder.build()).queue();
					});

					return;
				}
			}
		}
	}
}
