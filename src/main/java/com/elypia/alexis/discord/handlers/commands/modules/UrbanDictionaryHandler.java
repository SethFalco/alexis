package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.annotations.PostReactions;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.urbandictionary.UrbanDictionary;
import com.elypia.elypiai.urbandictionary.data.UrbanResultType;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	aliases = {"UrbanDictionary", "UrbanDict", "Urban", "UD"},
	help = "An online dictionary defined by the community for definitions and examples.",
	defaultCommand = "define"
)
public class UrbanDictionaryHandler extends CommandHandler {

	private UrbanDictionary dict;

	public UrbanDictionaryHandler() {
		dict = new UrbanDictionary();
	}

	@Command (aliases = "define", help = "Return the definition of a word or phrase.")
	@Parameter(name = "body", help = "Word or phrase to define!")
	@PostReactions({"🔉", "🎲"})
	public void define(MessageEvent event, String body) {
		dict.define(body, results -> {
			if (results.getResultType() == UrbanResultType.NO_RESULTS) {
				event.getChannel().sendMessage("Sorry I didn't find any results. :c").queue();
				return;
			}

			UrbanDefinition definition = results.getResult(true);

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
		}, failure -> BotUtils.httpFailure(event, failure));
	}

	@Command (aliases = "tags", help = "Return the tags associated with a word.")
	@Parameter(name = "body", help = "Word or phrase to define!")
	public void tags(MessageEvent event, String body) {
		dict.define(body, results -> {
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
		}, failure -> BotUtils.httpFailure(event, failure));
	}
}
