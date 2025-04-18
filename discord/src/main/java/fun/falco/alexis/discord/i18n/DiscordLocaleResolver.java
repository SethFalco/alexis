package fun.falco.alexis.discord.i18n;

import java.util.Locale;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.elypia.comcord.EventUtils;

import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.entities.MessageChannelData;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import fun.falco.alexis.core.persistence.repositories.MessageChannelRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;

/**
 * @author seth@falco.fun (Seth Falco)
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

    /** The Discord event that requires the locale. */
    private final transient GenericEvent event;

    @Inject
    public DiscordLocaleResolver(GuildRepository guildRepo, MessageChannelRepository messageChannelRepo, GenericEvent event) {
        this.guildRepo = guildRepo;
        this.messageChannelRepo = messageChannelRepo;
        this.event = event;
    }

    @Override
    public Locale getLocale() {
        MessageChannel channel = EventUtils.getMessageChannel(event);

        if (channel != null) {
            MessageChannelData channelData = messageChannelRepo.findBy(channel.getIdLong());

            if (channelData != null) {
                Locale channelLocale = channelData.getLocale();

                if (channelLocale != null) {
                    return channelLocale;
                }
            }
        }

        Guild guild = EventUtils.getGuild(event);

        if (guild != null) {
            GuildData guildData = guildRepo.findBy(guild.getIdLong());

            if (guildData != null) {
                Locale guildLocale = guildData.getLocale();

                if (guildLocale != null) {
                    return guildLocale;
                }
            }
        }

        return DEFAULT_LOCALE;
    }
}
