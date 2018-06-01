package com.elypia.alexis;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.entities.GreetingSettings;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.MessageSettings;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.Config;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.logging.Level;

public class EventHandler extends ListenerAdapter {

    private Chatbot chatbot;
	private Datastore store;

	public EventHandler(Chatbot chatbot) {
		this.chatbot = chatbot;
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

//		for (Guild guild : event.getJDA().getGuilds()) {
//			GuildData data = new GuildData();
//			data.setGuildId(guild.getIdLong());
//			store.save(data);
//		}
	}

	/**
	 * Occurs when the chatbot itself, joins a new guild.
	 *
	 * @param event GuildJoinEvent
	 */

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();
		TextChannel channel = guild.getDefaultChannel();

		if (channel != null) {
			String prefix = Config.getConfig("discord").getString("prefix");
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
		boolean bot = event.getUser().isBot();

		Guild guild = event.getGuild();
		Query<GuildData> query = store.createQuery(GuildData.class);
		GuildData data = query.filter("guild_id ==", guild.getIdLong()).get();

		GreetingSettings greeting = data.getSettings().getGreetingSettings();
		MessageSettings settings = bot ? greeting.getBotWelcome() : greeting.getUserWelcome();

		if (settings.isEnabled()) {
			JDA jda = event.getJDA();
			TextChannel channel = jda.getTextChannelById(settings.getChannel());

			List<String> messages = settings.getMessages();
			String message = messages.get(ElyUtils.RANDOM.nextInt(messages.size()));

			channel.sendMessage(message).queue();
		}
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

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
	public void onMessageReactionAdd(MessageReactionAddEvent event) {

	}
}
