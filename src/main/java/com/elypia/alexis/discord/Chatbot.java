package com.elypia.alexis.discord;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import com.elypia.alexis.AlexisUtils;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.*;
import com.elypia.alexis.discord.commands.impl.CommandHandler;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Chatbot {
	
	private JDA jda;
	private Map<List<String>, CommandHandler> handlers;
	
	public Chatbot(String botToken) throws LoginException, IllegalArgumentException, RateLimitedException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(botToken);
		builder.addEventListener(new DiscordDispatcher(this));
		builder.setCorePoolSize(8);
		builder.setGame(Game.playing("w/ Seth!"));
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);

		jda = builder.buildAsync();
		
		handlers = new HashMap<>();
	}
	
	public void registerModules(CommandHandler... handlers) {
		for (CommandHandler handler : handlers)
			registerModule(handler);
	}
	
	public void registerModule(CommandHandler handler) {
		Module module = handler.getClass().getAnnotation(Module.class);
		
		if (module == null) {
			AlexisUtils.LOGGER.log(Level.SEVERE, "{0} doesn't include the @Module annotation!", handler);
			return;
		}
	
		String[] aliases = module.aliases();
		
		for (int i = 0; i < aliases.length; i++)
			aliases[i] = aliases[i].toLowerCase();
		
		List<String> aliasesList = Arrays.asList(aliases);
		
		for (List<String> existing : handlers.keySet()) {
			for (String alias : aliasesList) {
				if (existing.contains(alias)) {
					String message = "{0} contains the alias {1}, which has already been registered!";
					Object[] params = {
						handler,
						alias
					};
					
					AlexisUtils.LOGGER.log(Level.WARNING, message, params);
					return;
				}
			}
		}
		
		handlers.put(aliasesList, handler);
	}
	
	public void handleMessage(MessageReceivedEvent event) {
		CommandEvent commandEvent = new CommandEvent(event);
		
		if (!commandEvent.isValid())
			return;
		else
			AlexisUtils.LOGGER.log(Level.INFO, event.getMessage().getContentRaw());
		
		String commandModule = commandEvent.getModule();
		CommandHandler handler = null;
		
		for (List<String> aliases : handlers.keySet()) {
			if (aliases.contains(commandModule)) {
				handler = handlers.get(aliases);
				break;
			}
		}
		
		if (handler == null) {
			AlexisUtils.LOGGER.log(Level.FINEST, "A user attmped to access the module {0}, which doesn't exist!", commandModule);
			return;
		}
		
		Method[] methods = handler.getClass().getMethods();
		Method commandMethod = null;
		
		for (Method method : methods) {
			Command command = method.getAnnotation(Command.class);
			
			if (command == null)
				continue;
			
			if (Arrays.asList(command.aliases()).contains(commandEvent.getCommand())) {
				commandMethod = method;
				break;
			}
		}
		
		if (commandMethod == null) {
			event.getChannel().sendMessage("That command doens't exist, try help?").queue();
			return;
		}
		
		try {
			commandMethod.invoke(handler, commandEvent);
		} catch (Exception ex) {
			AlexisUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
		}
	}
	
	public JDA getJDA() {
		return jda;
	}
}
