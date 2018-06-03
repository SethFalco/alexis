package com.elypia.alexis.handlers;

import com.elypia.alexis.Chatbot;
import com.elypia.alexis.entities.*;
import com.elypia.alexis.utils.*;
import com.elypia.elypiai.utils.ElyUtils;
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

import java.util.*;
import java.util.logging.Level;

public class EventHandler extends ListenerAdapter {

    private Chatbot chatbot;
    private MongoClient client;
    private Morphia morphia;
	private Datastore store;

	private InsertOptions insertOption;

	public EventHandler(Chatbot chatbot) {
		this.chatbot = chatbot;
		this.client = chatbot.getDatabase();
		this.morphia = chatbot.getMorphia();
		this.store = chatbot.getDatastore();

		insertOption = new InsertOptions();
		insertOption.continueOnError(true);
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

		Collection<GuildData> guildData = new ArrayList<>();

		event.getJDA().getGuilds().forEach(guild -> {
			GuildData data = new GuildData();
			data.setGuildId(guild.getIdLong());
			guildData.add(data);
		});

		store.save(guildData, insertOption);

		Collection<UserData> userData = new ArrayList<>();

		event.getJDA().getUsers().forEach(user -> {
			UserData data = new UserData();
			data.setUserId(user.getIdLong());
			userData.add(data);
		});

		store.save(userData, insertOption);
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
		store.save(data, insertOption);
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
	public void onMessageReceived(MessageReceivedEvent event) {

	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {

	}
}
