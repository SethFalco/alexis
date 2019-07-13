package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.elypiai.common.core.ex.FriendlyException;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.elypia.elypiai.nanowrimo.data.NanoError;
import com.google.inject.Inject;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.*;

import javax.validation.constraints.Size;

@Module(name = "nano", aliases = {"nanowrimo", "nano", "nnwm"}, help = "nano.help")
public class NanowrimoModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(NanowrimoModule.class);

    private Nanowrimo nano;

    @Inject
    public NanowrimoModule(Nanowrimo nano) {
        this.nano = nano;
    }

    @Command(name = "nano.info", aliases = {"get", "info"}, help = "nano.info.help")
    public void getUser(
        CommandlerEvent<MessageReceivedEvent> event,
        @Param(name = "common.username", help = "nano.info.p.name.h") @Size(min = 1, max = 60) String name
    ) {
        nano.getUser(name, true).queue((user) -> {
            event.send(user);
        }, (ex) -> {
            if (ex instanceof FriendlyException) {
                FriendlyException fex = (FriendlyException)ex;

                switch (fex.getTag()) {
                    case NanoError.USER_DOES_NOT_EXIST: {
                        event.send("nano.no_user");
                        return;
                    }
                    case NanoError.USER_DOES_NOT_HAVE_A_CURRENT_NOVEL: {
                        event.send("nano.info.no_novel");
                        return;
                    }
                }
            }
            else
                logger.error("Failed to perform HTTP request!", ex);
        });
    }
}
