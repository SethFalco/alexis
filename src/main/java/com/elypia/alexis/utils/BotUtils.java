package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.ChatbotConfiguration;
import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.CommandHandler;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotUtils {

	private BotUtils() {
		// Unable to instantiate this class.
	}

	public static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	public static void unirestFailure(UnirestException failure, CommandEvent event) {
		// Log the exception to console, was likely time out or API in use is deprecated.
		LOGGER.log(Level.SEVERE, "Unirest request failed.", failure);

		// Let the user know what happened and apologise.
		String message = "Sorry! I'm don't know why the command failed but I'm reporting this to Seth, perhaps trying again later?";
		event.getChannel().sendMessage(message).queue();
	}

	public static ChatbotConfiguration getConfiguration(String path) {
		try (FileReader reader = new FileReader(path)) {
			StringBuilder builder = new StringBuilder();
			int i;

			while ((i = reader.read()) != -1)
				builder.append((char)i);

			JSONObject object = new JSONObject(builder.toString());
			return new ChatbotConfiguration(object);
		} catch (FileNotFoundException ex) {
			JSONObject object = ChatbotConfiguration.generateConfigTemplate();

			try (FileWriter writer = new FileWriter(path)) {
				String json = object.toString(4);
				writer.write(json);

				ExitCode code = ExitCode.GENERATED_NEW_CONGIG;
				BotUtils.LOGGER.log(Level.INFO, code.getMessage() + path);
				writer.close();
				System.exit(code.getStatusCode());
			} catch (IOException e) {
				ExitCode code = ExitCode.FAILED_TO_WRITE_CONFIG;
				BotUtils.LOGGER.log(Level.SEVERE, code.getMessage() + path, e);
				System.exit(code.getStatusCode());
			}
		} catch (IOException ex){
			ExitCode code = ExitCode.FAILED_TO_READ_CONFIG;
			BotUtils.LOGGER.log(Level.SEVERE, code.getMessage() + path, ex);
			System.exit(code.getStatusCode());
		}

		return null;
	}

	public static Module getModule(CommandHandler handler) {
		return handler.getClass().getAnnotation(Module.class);
	}

	public static Collection<Command> getCommands(CommandHandler handler) {
		Collection<Command> commands = new ArrayList<>();
		Method[] methods = handler.getClass().getMethods();

		for (Method method : methods) {
			Command command = method.getAnnotation(Command.class);

			if (command != null)
				commands.add(command);
		}

		return commands;
	}

	public static Member findMember(Guild guild, String term) {
		if (guild == null || term == null)
			return null;

		List<Member> members = guild.getMembers();
		Set<Member> results = new HashSet<>();

		members.forEach(member -> {
			Member m = guild.getMemberById(term);

			if (m != null)
				results.add(m);

			if (member.getNickname().equalsIgnoreCase(term))
				results.add(m);

			if (member.getAsMention().equals(term))
				results.add(m);

			if (member.getUser().getName().equalsIgnoreCase(term))
				results.add(m);

		});

		return null;
	}
}
