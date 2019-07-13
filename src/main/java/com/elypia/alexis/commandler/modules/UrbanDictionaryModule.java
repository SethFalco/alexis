package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.elypiai.common.core.RestLatch;
import com.elypia.elypiai.urbandictionary.*;
import com.google.inject.*;
import org.slf4j.*;

// TODO: Allow UrbanDictionary to specify which definition by index.
@Singleton
@Module(name = "ud", aliases = {"urbandictionary", "urbandict", "urban", "ud"}, help = "ud.help")
public class UrbanDictionaryModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryModule.class);

	private UrbanDictionary ud;

	@Inject
    public UrbanDictionaryModule(UrbanDictionary ud) {
        this.ud = ud;
    }

    @Static
    @Command(name = "ud.define", aliases = "define", help = "urban.define.h")
	public void define(
        CommandlerEvent<?> event,
        @Param(name = "p.body", help = "ud.define.body") String[] terms,
        @Param(name = "p.random", help = "ud.define.random", defaultValue = "false") boolean random
    ) {
        RestLatch<DefineResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) ->
            event.send((!results.isEmpty()) ? results : "urban.define.no_result")
        );
    }
}
