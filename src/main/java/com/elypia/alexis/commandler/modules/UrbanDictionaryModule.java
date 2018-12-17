package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.elypiai.restutils.RestLatch;
import com.elypia.elypiai.urbandictionary.*;
import com.elypia.elypiai.urbandictionary.data.ResultType;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.EmbedBuilder;
import org.slf4j.*;

@Module(id = "Urban Dictionary", aliases = {"urbandictionary", "urbandict", "urban", "ud"}, help = "urban.h")
public class UrbanDictionaryModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryModule.class);

	private UrbanDictionary ud;

    public UrbanDictionaryModule() {
        ud = new UrbanDictionary();
    }

	@Static
	@Command(id = "Define", aliases = "define", help = "urban.define.h")
	@Param(id = "common.body", help = "urban.define.p.body.h")
//	@Emoji(emotes = "🔉", help = "urban.define.e.speaker.h")
//	@Emoji(emotes = "🎲", help = "urban.define.e.random.h")
	public void define(JDACEvent event, String[] terms) {
		define(event, terms, true);
	}

    @Overload("Define")
    @Param(id = "common.random", help = "urban.define.p.random.h")
	public void define(JDACEvent event, String[] terms, boolean random) {
        RestLatch<UrbanResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) ->
            event.send((!results.isEmpty()) ? results : "urban.define.no_result")
        );
    }

//	@Reaction(id = 50, emotes = "🎲")
//	public UrbanDefinition swapDefinition(ReactionEvent event) {
//		UrbanResult results = (UrbanResult)event.getReactionRecord().getObject("results");
//		return results.getDefinition(true);
//	}
//
	@Command(id = "urban.tags", aliases = "tags", help = "urban.tags.h")
	@Param(id = "common.body", help = "urban.tags.p.body.h")
	public void tags(JDACEvent event, String body) {
        ud.define(body).queue(results -> {
            if (results.getResultType() == ResultType.NO_RESULTS) {
                event.send("urban.define.no_result");
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();

            String titleText = results.getDefinition().getWord();
            builder.setTitle(titleText);

            String tagsText = String.join(", ", results.getTags());
            builder.addField(scripts.get("urban.tags.tags"), tagsText, true);

            event.send(builder);
        }, failure -> logger.error("HTTP request failed.", failure));
	}
}
