package com.elypia.alexis.commandler;

import com.elypia.commandler.MisuseHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

public class AlexisMisuseHandler extends MisuseHandler<GenericMessageEvent, Message> {

    private static Logger logger = LoggerFactory.getLogger(AlexisMisuseHandler.class);

    @Override
    public <X extends Exception> String onException(X ex) {
        logger.error("An unknown error occured.", ex);
        return "A teddy bear had been spotted stealing a line of code, it seems it broke some of our functionality";
    }
}
