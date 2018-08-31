package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.DiscordLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.Emoji;
import com.elypia.elypiai.urbandictionary.*;
import com.elypia.elypiai.urbandictionary.data.UrbanResultType;
import com.elypia.elypiai.utils.okhttp.RestLatch;
import com.elypia.elyscript.ElyScript;

@Module(name = "Urban Dictionary", aliases = {"urbandictionary", "urbandict", "urban", "ud"}, help = "An online dictionary defined by the community for definitions and examples.")
public class UrbanDictionaryHandler extends JDAHandler {

    private ElyScript NO_RESULTS = new ElyScript("(Sorry|I apologise)(,|...) I (didn't|couldn't|(did|could) not|was unable to) find( any){?} results( for that){?}( on UrbanDictionary){?}.( :c){?}");

	private UrbanDictionary ud;

    public UrbanDictionaryHandler() {
        ud = new UrbanDictionary();
    }

	@Static
	@Command(id = 50, name = "Define", aliases = "define", help = "Return the definition of a word or phrase.")
	@Param(name = "body", help = "Word or phrase to define!")
	@Emoji(emotes = "🔉", help = "Hear an audio clip associtated with this word.")
	@Emoji(emotes = "🎲", help = "Don't like definition? Get a new one!")
	public void define(JDACommand event, String[] terms) {
		define(event, terms, true);
	}

    @Overload(value = 50)
    @Param(name = "random", help = "Random result or top result!")
	public void define(JDACommand event, String[] terms, boolean random) {
        if (terms.length == 1)
            defineSingle(event, terms[0], random);
        else
            defineMulti(event, terms, random);
    }

    private void defineSingle(JDACommand event, String body, boolean random) {
        ud.define(body).queue((result) -> {
            if (result.getResultType() == UrbanResultType.NO_RESULTS) {
                event.reply(NO_RESULTS.compile());
                return;
            }

//            event.storeObject("results", result);

            UrbanDefinition definition = result.getDefinition(random);
            event.reply(definition);
        }, (ex) -> DiscordLogger.log(event, ex));
    }

	private void defineMulti(JDACommand event, String[] terms, boolean random) {
        RestLatch<UrbanResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) -> {
            if (results.isEmpty())
                event.reply("Sorry, I couldn't get a result for any of those words.");
            else
                event.reply(results);
        });
    }

//	@Reaction(id = 50, emotes = "🎲")
//	public UrbanDefinition swapDefinition(ReactionEvent event) {
//		UrbanResult results = (UrbanResult)event.getReactionRecord().getObject("results");
//		return results.getDefinition(true);
//	}
//
//	@Command(name = "Get Associated Tags", aliases = "tags", help = "Return the tags associated with a word.")
//	@Param(name = "body", help = "Word or phrase to define!")
//	public void tags(JDACommand event, String body) {
//		dict.define(body, results -> {
//			if (results() == UrbanResultType.NO_RESULTS) {
//				event.getMessageEvent().getChannel().sendMessage("Sorry I didn't get a result. :c").queue();
//				return;
//			}
//
//			EmbedBuilder builder = new EmbedBuilder();
//
//			String titleText = results.getSearchTerm();
//			builder.setTitle(titleText);
//
//			String tagsText = String.join(", ", results.getTags());
//			builder.addField("Tags", tagsText, true);
//
//			event.reply(builder);
//		}, failure -> BotUtils.sendHttpError(event, failure));
//	}
}
