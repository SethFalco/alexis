package com.elypia.alexis;

import com.elypia.alexis.entities.AlexisError;
import com.elypia.commandler.MisuseHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.*;

public class AlexisMisuseHandler extends MisuseHandler<GenericMessageEvent, Message> {

    private static Logger logger = LoggerFactory.getLogger(AlexisMisuseHandler.class);

    @Override
    public <X extends Exception> String onException(X ex) {
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        new AlexisError(stacktrace).commit();

        logger.error("An unknown error occured.", ex);

        return "A teddy bear had been spotted stealing a line of code, it seems it broke some of our functionality";
    }
}
