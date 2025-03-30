package fun.falco.alexis.discord.listeners;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * Log the events received from Discord with the log level.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 3.0.0
 */
@Singleton
public class LoggingListener implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void onEvent(GenericEvent event) {
        logger.debug("Received {} from Discord.", event.getClass().getSimpleName());
    }
}
