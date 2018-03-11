package com.elypia.alexis.discord;

import com.elypia.alexis.discord.annotation.BeforeAny;
import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.CommandEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.utils.ElyUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Chatbot {

	private JDA jda;
	private MongoClient client;
	private Map<String[], CommandHandler> handlers;

	private List<Game> statuses;

	private ChatbotConfiguration config;

	public Chatbot(ChatbotConfiguration config, MongoClient client) throws LoginException {
		this.config = config;

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(config.getToken());
		builder.addEventListener(new DiscordDispatcher(this));
		builder.setCorePoolSize(10);
		builder.setGame(Game.playing(config.getDefaultStatuses()[0]));
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		jda = builder.buildAsync();

		this.client = client;

		for (String status : config.getDefaultStatuses()) {

		}

		handlers = new HashMap<>();
	}

	public void registerModules(CommandHandler... handlers) {
		for (CommandHandler handler : handlers)
			registerModule(handler);
	}

	public void registerModule(CommandHandler handler) {
		Module module = handler.getClass().getAnnotation(Module.class);

		if (module == null) {
			BotUtils.LOGGER.log(Level.SEVERE, "{0} doesn't include the @Module annotation!", handler);
			return;
		}

		String[] aliases = module.aliases();

		for (int i = 0; i < aliases.length; i++)
			aliases[i] = aliases[i].toLowerCase();

		for (String[] existing : handlers.keySet()) {
			if (ElyUtils.containsAny(existing, aliases)) {
				String message = "{0} contains an alias which has already been registered!";
				BotUtils.LOGGER.log(Level.WARNING, message, handler);
				return;
			}
		}

		handlers.put(aliases, handler);
	}

	public void handleMessage(MessageReceivedEvent event) {
		CommandEvent commandEvent = new CommandEvent(this, event);

		if (!commandEvent.isValid())
			return;

		BotUtils.LOGGER.log(Level.INFO, event.getMessage().getContentRaw());

		String commandModule = commandEvent.getModule();
		CommandHandler handler = null;

		for (String[] aliases : handlers.keySet()) {
			if (ElyUtils.arrayContains(commandModule, aliases)) {
				handler = handlers.get(aliases);
				break;
			}
		}

		if (handler == null) {
			BotUtils.LOGGER.log(Level.FINEST, "A user attmped to access the module {0}, which doesn't exist!", commandModule);
			return;
		}

		Method[] methods = handler.getClass().getMethods();
		Method commandMethod = null;
		Command command = null;

		for (Method method : methods) {
			BeforeAny beforeAny = method.getAnnotation(BeforeAny.class);

			if (beforeAny != null) {
				String[] exclusions = beforeAny.value();

				if (!ElyUtils.arrayContains(commandEvent.getCommand(), exclusions)) {
					if (handler.beforeAny(commandEvent))
						return;
				}
			}

			command = method.getAnnotation(Command.class);

			if (command == null)
				continue;

			if (ElyUtils.arrayContains(commandEvent.getCommand(), command.aliases()))
				commandMethod = method;
		}

		if (commandMethod == null) {
			event.getChannel().sendMessage("That command doens't exist, try help?").queue();
			return;
		}

		commandEvent.setMethod(commandMethod);

		try {
			commandMethod.invoke(handler, commandEvent);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			BotUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
		}
	}

	public JDA getJDA() {
		return jda;
	}

	public ChatbotConfiguration getConfig() {
		return config;
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
}
