package com.elypia.alexis;

import java.util.logging.*;

import com.elypia.alexis.discord.commands.CommandEvent;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AlexisUtils {
	
	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());
	
	public static void unirestFailure(UnirestException failure, CommandEvent event) {
		// Log the exception to console, was likely time out or API in use is deprecated.
		LOGGER.log(Level.SEVERE, "Unirest request failed.", failure);
		
		// Let the user know what happened and apologise.
		String message = "Sorry! I'm don't know why the command failed but I'm reporting this to Seth, perhaps trying again later?";
		event.getChannel().sendMessage(message).queue();
	}
}
