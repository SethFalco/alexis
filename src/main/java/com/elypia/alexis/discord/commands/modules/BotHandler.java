package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.*;
import com.elypia.alexis.discord.commands.impl.CommandHandler;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Module (
	aliases = {"Bot", "Robot"},
	description = "Bot commands for stats or information."
)
public class BotHandler extends CommandHandler {
	
	@Override
	public boolean test() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Command (
		aliases = "ping",
		help = "Respond 'pong!' with the number of `ms` it took to fulfil the request!"
	)
	public void ping(CommandEvent event) {
		pingPong(event, "pong!");
	}
	
	@Command (
		aliases = "pong",
		help = ""
	)
	public void pong(CommandEvent event) {
		pingPong(event, "ping!");
	}
	
	private void pingPong(CommandEvent event, String text) {
		long startTime = System.currentTimeMillis();
		
		event.getChannel().sendMessage(text).queue(message -> {
			long endTime = System.currentTimeMillis() - startTime;
			
			message.editMessage(String.format("%s `%,dms`", message.getContentRaw(), endTime)).queue();
		});
	}
	
	@Command (
		aliases = {"stats", "info"},
		help = "Display stats on Alexis!"
	)
	public void displayStats(CommandEvent event) {
		JDA jda = event.getJDA();
		User alexis = jda.getSelfUser();
		
		EmbedBuilder embedB = new EmbedBuilder();
		embedB.setTitle(String.format("%s stats!", alexis.getName()));
		embedB.setThumbnail(alexis.getAvatarUrl());
		embedB.addField("Guilds", String.valueOf(jda.getGuilds().size()), false);
		embedB.addField("Users", String.valueOf(jda.getUsers().size()), false);
		
		event.getChannel().sendMessage(embedB.build()).queue();
	}
	
	@Command (
		aliases = "say",
		help = "Have Alexis repeat something you say!",
		params = {
			@Parameter (param = "body", help = "Text Alexis should repeat!", type = String.class)
		},
		optParams = {
			@OptParameter (param = "delete", help = "Should Alexis delete the message after?", defaultValue = "false")
		}
	)
	public void say(CommandEvent event) {
		event.getChannel().sendMessage(event.getParams()[0]).queue();
		
		MessageReceivedEvent messageEvent = event.getMessageEvent();
		
		if (messageEvent.getChannelType() == ChannelType.TEXT) {
			if (event.getSelfMember().hasPermission(messageEvent.getTextChannel(), Permission.MESSAGE_MANAGE)) {
				event.getMessage().delete().queue();
			}
		}
	}
}
