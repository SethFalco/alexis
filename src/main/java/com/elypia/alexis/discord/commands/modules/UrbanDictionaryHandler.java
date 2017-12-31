package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.AlexisUtils;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.*;
import com.elypia.alexis.discord.commands.impl.CommandHandler;
import com.sethsutopia.utopiai.urbandictionary.*;

import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	aliases = {"UrbanDictionary", "UrbanDict", "Urban", "UD"},
	description = "An online dictionary defined by the community for words, definitions and examples."
)
public class UrbanDictionaryHandler extends CommandHandler {
	
	private UrbanDictionary dict;
	
	public UrbanDictionaryHandler() {
		dict = new UrbanDictionary();
	}
	
	@Override
	public boolean test() {

		return false;
	}
	
	@Command (
		aliases = "define",
		help = "Return the definition of a word or phrase.",
		params = {
			@Parameter (param = "body", help = "Word or phrase to define!", type = String.class)
		},
		optParams = {
			@OptParameter (param = "random", help = "Want the top definition or a random one?", defaultValue = "true")
		}
	)
	public void define(CommandEvent event) {
		dict.define(event.getParams()[0], results -> {
			
			if (results.getResultType() == UrbanResultType.NO_RESULTS) {
				event.getChannel().sendMessage("Sorry I didn't find any results. :c").queue();
				return;
			}
			
			UrbanDefinition definition = results.getRandomResult();
			
			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(definition.getAuthor());
			
			String titleText = String.format("Definition of: '%s'", definition.getWord());
			builder.setTitle(titleText, definition.getPermaLink());
			
			builder.setDescription(definition.getDefinition());
			builder.addField("Example", definition.getExample(), true);
			
			String footerText = String.format("👍: %,d 👎: %,d", definition.getThumbsUp(), definition.getThumbsDown());
			builder.setFooter(footerText, null);
			
			event.getChannel().sendMessage(builder.build()).queue();
		}, failure -> {
			AlexisUtils.unirestFailure(failure, event);
		});
	}
}
