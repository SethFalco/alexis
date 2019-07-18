package com.elypia.alexis;

import com.elypia.commandler.misuse.CommandMisuseListener;
import org.slf4j.*;

public class AlexisMisuseHandler extends CommandMisuseListener {

    private static Logger logger = LoggerFactory.getLogger(AlexisMisuseHandler.class);

    @Override
    public <X extends Exception> String onException(X ex) {
        logger.error("An unknown error occured.", ex);
        return "A teddy bear had been spotted stealing a line of code, it seems it broke some of our functionality";
    }
}
