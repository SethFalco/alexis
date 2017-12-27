package com.elypia.alexis.discord;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.commands.*;
import com.elypia.alexis.discord.commands.annotation.*;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Chatbot {
	
	private JDA jda;
	private Map<String, CommandHandler> handlers;
	
	public Chatbot(String botToken) throws LoginException, IllegalArgumentException, RateLimitedException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(botToken);
		builder.addEventListener(new DiscordDispatcher(this));
		builder.setGame(Game.playing("w/ Seth!"));
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);

		jda = builder.buildAsync();
		
		handlers = new HashMap<>();
	}
	
	public void registerModule(CommandHandler handler) {
		Module module = handler.getClass().getAnnotation(Module.class);
		
		if (module == null) {
			Alexis.LOGGER.log(Level.SEVERE, "{0} doesn't include the @Module annotation!", handler);
			return;
		}
		
		String name = module.name().toLowerCase();
		
		if (handlers.containsKey(name))
			Alexis.LOGGER.log(Level.WARNING, "Module {0} was already registered!", module);
		else
			handlers.put(name, handler);
	}
	
	public void handleMessage(MessageReceivedEvent event) {
		CommandEvent commandEvent = new CommandEvent(event);
		
		if (!commandEvent.isValid())
			return;
		
		CommandHandler handler = handlers.get(commandEvent.getModule());
		
		Method[] methods = handler.getClass().getMethods();
		
		for (Method method : methods) {
			Command command = method.getAnnotation(Command.class);
			
			if (command != null) {				
				if (command.alias().equalsIgnoreCase(commandEvent.getCommand())) {
					try {
						method.invoke(handler, commandEvent);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	public JDA getJda() {
		return jda;
	}
}
