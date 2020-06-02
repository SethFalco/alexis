package org.elypia.alexis.discord.i18n;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.event.Request;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.Locale;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
@Priority(1)
@Alternative
@RequestScoped
public class DiscordLocaleResolver implements LocaleResolver {

    /** The default locale if none is specified. */
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    private final transient GuildRepository guildRepo;
    private final transient MessageChannelRepository messageChannelRepo;

    private final transient Request<?, ?> request;

    @Inject
    public DiscordLocaleResolver(GuildRepository guildRepo, MessageChannelRepository messageChannelRepo, Request request) {
        this.guildRepo = guildRepo;
        this.messageChannelRepo = messageChannelRepo;
        this.request = request;
    }

    @Override
    public Locale getLocale() {
        Object source = request.getSource();

        if (!(source instanceof Event))
            return DEFAULT_LOCALE;

        Event sourceEvent = (Event)source;

        MessageChannel channel = EventUtils.getMessageChannel(sourceEvent);

        if (channel != null) {
            MessageChannelData channelData = messageChannelRepo.findBy(channel.getIdLong());

            if (channelData != null) {
                Locale channelLocale = channelData.getLocale();

                if (channelLocale != null)
                    return channelLocale;
            }
        }

        Guild guild = EventUtils.getGuild(sourceEvent);

        if (guild != null) {
            GuildData guildData = guildRepo.findBy(guild.getIdLong());

            if (guildData != null) {
                Locale guildLocale = guildData.getLocale();

                if (guildLocale != null)
                    return guildLocale;
            }
        }

        return DEFAULT_LOCALE;
    }
}
