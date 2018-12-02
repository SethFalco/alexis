package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.urbandictionary.*;
import com.elypia.elypiai.urbandictionary.data.UrbanResultType;
import com.elypia.elypiai.utils.okhttp.RestLatch;
import net.dv8tion.jda.core.EmbedBuilder;
import org.slf4j.*;

@Module(name = "urban", aliases = {"urbandictionary", "urbandict", "urban", "ud"}, help = "urban.h")
public class UrbanDictionaryHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryHandler.class);

	private UrbanDictionary ud;

    public UrbanDictionaryHandler() {
        ud = new UrbanDictionary();
    }

	@Static
	@Command(id = 50, name = "urban.define", aliases = "define", help = "urban.define.h")
	@Param(name = "common.body", help = "urban.define.p.body.h")
	@Emoji(emotes = "🔉", help = "urban.define.e.speaker.h")
	@Emoji(emotes = "🎲", help = "urban.define.e.random.h")
	public void define(JDACommand event, String[] terms) {
		define(event, terms, true);
	}

    @Overload(value = 50)
    @Param(name = "common.random", help = "urban.define.p.random.h")
	public void define(JDACommand event, String[] terms, boolean random) {
        if (terms.length == 1)
            defineSingle(event, terms[0], random);
        else
            defineMulti(event, terms, random);
    }

    private void defineSingle(JDACommand event, String body, boolean random) {
        ud.define(body).queue((result) -> {
            if (result.getResultType() == UrbanResultType.NO_RESULTS) {
                event.replyScript("urban.define.no_result");
                return;
            }

//            event.storeObject("results", result);

            UrbanDefinition definition = result.getDefinition(random);
            event.reply(definition);
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }

	private void defineMulti(JDACommand event, String[] terms, boolean random) {
        RestLatch<UrbanResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) -> {
            if (results.isEmpty())
                event.replyScript("urban.define.no_result");
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
	@Command(name = "urban.tags", aliases = "tags", help = "urban.tags.h")
	@Param(name = "common.body", help = "urban.tags.p.body.h")
	public void tags(JDACommand event, String body) {
        ud.define(body).queue(results -> {
            if (results.getResultType() == UrbanResultType.NO_RESULTS) {
                event.replyScript("urban.define.no_result");
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();

            String titleText = results.getDefinition().getWord();
            builder.setTitle(titleText);

            String tagsText = String.join(", ", results.getTags());
            builder.addField(event.getScript("urban.tags.tags"), tagsText, true);

            event.reply(builder);
        }, failure -> logger.error("HTTP request failed.", failure));
	}
}
