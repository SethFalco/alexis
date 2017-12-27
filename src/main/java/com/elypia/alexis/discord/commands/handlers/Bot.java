package com.elypia.alexis.discord.commands.handlers;

import com.elypia.alexis.discord.commands.*;
import com.elypia.alexis.discord.commands.annotation.*;

@Module (
	name = "Bot",
	description = "Bot commands for stats or information."
)
public class Bot extends CommandHandler {
	
	@Command (
		alias = "ping",
		help = "Respond pong with the number of `ms` it took to fulfil the request!"
	)
	public void ping(CommandEvent event) {
		long startTime = System.currentTimeMillis();
		
		event.getMessageReceivedEvent().getChannel().sendMessage("pong!").queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;
			
			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}
}
