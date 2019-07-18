package com.elypia.alexis.modules;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.managers.*;
import com.elypia.elypiai.common.core.RestLatch;
import com.elypia.elypiai.urbandictionary.*;
import com.google.inject.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

// TODO: Allow Urban Dictionary to specify which definition by index.
@Singleton
@Module(name = "Urban Dictionary", aliases = {"ud", "urban", "urbandict", "urbandictionary"})
public class UrbanDictionaryModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryModule.class);

    private final ResponseManager responseManager;
    private final LanguageManager lang;
    private final UrbanDictionary ud;

	@Inject
    public UrbanDictionaryModule(ResponseManager responseManager, LanguageManager lang, UrbanDictionary ud) {
        this.responseManager = responseManager;
        this.lang = lang;
        this.ud = ud;
    }

    @Static
    @Command(name = "ud.define", aliases = "define")
	public void define(
        CommandlerEvent<GenericMessageEvent> event,
        @Param(name = "body") String[] terms,
        @Param(name = "random", defaultValue = "false") boolean random
    ) {
        RestLatch<DefineResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) -> {
            if (results.isEmpty())
                event.getSource().getChannel().sendMessage(lang.get(event, "urban.no_result"));
            else {
                Message message = (Message) responseManager.provide(event, results);
                event.getSource().getChannel().sendMessage(message).queue();
            }
        });
    }
}
