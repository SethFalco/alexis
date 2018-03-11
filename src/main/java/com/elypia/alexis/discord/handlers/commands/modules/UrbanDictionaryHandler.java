package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.*;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.urbandictionary.UrbanDictionary;
import com.elypia.elypiai.urbandictionary.UrbanResultType;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	aliases = {"UrbanDictionary", "UrbanDict", "Urban", "UD"},
	help = "An online dictionary defined by the community for definitions and examples."
)
public class UrbanDictionaryHandler extends CommandHandler {

	private UrbanDictionary dict;

	public UrbanDictionaryHandler() {
		dict = new UrbanDictionary();
	}

	@Command (
		aliases = "define",
		help = "Return the definition of a word or phrase.",
		params = {
			@Parameter (
				param = "body",
				help = "Word or phrase to define!",
				type = String.class
			)
		},
		optParams = {
			@OptParameter (
				param = "random",
				help = "Want the top definition or a random one?",
				type = Boolean.class,
				defaultValue = "true"
			)
		},
		reactions = { "🔉", "🎲"}
	)
	public void define(MessageEvent event) {
		dict.define(event.getParams()[0], results -> {
			if (results.getResultType() == UrbanResultType.NO_RESULTS) {
				event.getChannel().sendMessage("Sorry I didn't find any results. :c").queue();
				return;
			}

			UrbanDefinition definition = results.getRandomResult();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(definition.getAuthor());

			String titleText = definition.getWord();
			builder.setTitle(titleText, definition.getPermaLink());

			builder.setDescription(definition.getDefinition());

			String descText = String.format (
				"%s\n\n👍: %,d 👎: %,d",
				definition.getExample(),
				definition.getThumbsUp(),
				definition.getThumbsDown()
			);
			builder.addField("Example", descText, true);

			event.reply(builder);
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Command (
		aliases = "tags",
		help = "Return the tags associated with a word.",
		params = {
			@Parameter (
				param = "body",
				help = "Word or phrase to define!",
				type = String.class
			)
		}
	)
	public void tags(MessageEvent event) {
		dict.define(event.getParams()[0], results -> {
			if (results.getResultType() == UrbanResultType.NO_RESULTS) {
				event.getChannel().sendMessage("Sorry I didn't get a result. :c").queue();
				return;
			}

			EmbedBuilder builder = new EmbedBuilder();

			String titleText = results.getSearchTerm();
			builder.setTitle(titleText);

			String tagsText = String.join(", ", results.getTags());
			builder.addField("Tags", tagsText, true);

			event.reply(builder);
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Reaction (
		aliases = "🎲",
		command = "define"
	)
	public void anotherRandomDefinition(MessageEvent event) {
		define(event);
	}
}
