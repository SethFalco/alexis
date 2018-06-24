package com.elypia.alexis.handlers;

import com.elypia.alexis.*;
import com.elypia.alexis.entities.*;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.google.translate.GoogleTranslate;
import com.elypia.elypiai.utils.*;
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
import org.apache.commons.lang3.StringEscapeUtils;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.*;

import java.util.*;
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
		BotUtils.log(Level.INFO, "Time taken to launch: %,dms", timeElapsed);

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

		if (channel != null) {
			String prefix = Alexis.getConfig().getDiscordConfig().getPrefix();
			String message = "Thank you for inviting me! My default prefix is `" + prefix + "` but you can mention me too!\nFeel free to try my help command!";
			channel.sendMessage(message).queue();
		}

		BotUtils.log(Level.INFO, "The guild " + guild.getName() + " just invited me!");
		BotUtils.logBotInfo();

		GuildData data = new GuildData();
		data.setGuildId(guild.getIdLong());
		store.save(data);
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Guild guild = event.getGuild();

		BotUtils.log(Level.INFO, "The guild " + guild.getName() + " just kicked me!");
		BotUtils.logBotInfo();
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
			message = BotUtils.buildCustomMessage(event, message);

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
		if (!event.isFromType(ChannelType.TEXT))
			return;

		if (event.getGuild().getMembers().stream().filter(o -> !o.getUser().isBot()).count() == 1)
			return;

		User user = event.getAuthor();
		UserData data = UserData.query(user.getIdLong());

		int entitlement = data.getXpEntitlement(event);
		UpdateOperations<UserData> update = store.createUpdateOperations(UserData.class);

		if (entitlement > 0)
			update.set("xp", data.getXp() + entitlement);

		update.set("last_active", new Date());
		store.update(data, update);
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if (event.getUser().isBot())
			return;

		handleTranslate(event);
	}

	GoogleTranslate translate = new GoogleTranslate("***REMOVED***");

	private void handleTranslate(MessageReactionAddEvent event) {
		List<Language> languages = translate.getSupportedLanguages();

		for (Language language : languages) {
			for (Country country : language.getCountries()) {
				if (country.getUnicodeEmote().equals(event.getReactionEmote().getName())) {
					event.getChannel().getMessageById(event.getMessageId()).queue(message -> {
						translate.translate(message.getContentStripped(), language, result -> {
							EmbedBuilder builder = new EmbedBuilder();
							builder.addField("Source (" + result.getSource().getLanguageName() + ")", result.getBody() + "\n_ _", false);
							String translatedBody = StringEscapeUtils.unescapeHtml4(result.getTranslatedBody());
							builder.addField("Target (" + result.getTarget().getLanguageName() + ")", translatedBody, false);
							builder.setImage("https://cdn.discordapp.com/attachments/436154993247256586/460187735936991233/color-short2x.png");
							builder.setFooter("http://translate.google.com/", null);
							event.getChannel().sendMessage(builder.build()).queue();
							message.addReaction(country.getUnicodeEmote()).queue();
						}, ex -> BotUtils.sendHttpError(null, ex));
					});
				}
			}
		}
	}
}
