package com.elypia.alexis.discord;

import com.elypia.alexis.discord.handlers.GlobalMessageHandler;
import com.elypia.alexis.discord.handlers.GlobalReactionHandler;
import com.elypia.alexis.discord.managers.impl.DiscordManager;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Chatbot {

	private JDA jda;

	private GlobalMessageHandler globalMessageHandler;
	private GlobalReactionHandler globalReactionHandler;

	private MongoClient client;

	private List<Game> statuses;

	public Chatbot(MongoClient client) throws LoginException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(Config.token);
		builder.setCorePoolSize(10);
//		builder.setGame(Game.playing(config.getDefaultStatuses()[0]));
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);

		jda = builder.buildAsync();

		this.client = client;
		globalMessageHandler = new GlobalMessageHandler(client);
	}

	public void registerManagers(DiscordManager... managers) {
		jda.addEventListener((Object[])managers);
	}

	public JDA getJDA() {
		return jda;
	}

	public MongoClient getClient() {
		return client;
	}

	/**
	 * @return The default database which stores the bulk of the chatbots data.
	 */

	public MongoDatabase getHomeDatabase() {
		return getDatabase("alexis");
	}

	public MongoDatabase getDatabase(String database) {
		return client.getDatabase(database);
	}

	public GlobalMessageHandler getGlobalMessageHandler() {
		return globalMessageHandler;
	}
}
