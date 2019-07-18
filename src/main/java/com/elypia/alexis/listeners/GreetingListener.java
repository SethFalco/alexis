package com.elypia.alexis.listeners;

import com.elypia.alexis.entities.*;
import com.elypia.alexis.services.DatabaseService;
import com.google.inject.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hibernate.Session;
import org.slf4j.*;

import java.util.Objects;

@Singleton
public class GreetingListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GreetingListener.class);

    private final DatabaseService dbService;

    @Inject
    public GreetingListener(final DatabaseService dbService) {
        this.dbService = Objects.requireNonNull(dbService);

        if (dbService.isDisabled())
            logger.warn("GreetingListener instantiated but Database isn't enabled so no greetings will be sent.");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        onGuildMemberEvent(event, true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        onGuildMemberEvent(event, false);
    }

    public void onGuildMemberEvent(GenericGuildMemberEvent event, boolean join) {
        if (dbService.isDisabled())
            return;

        boolean bot = event.getUser().isBot();

        Guild guild = event.getGuild();

        try (Session session = dbService.open()) {
            GuildData data = session.get(GuildData.class, guild.getIdLong());
            GuildFeature feature;

            if (join && bot)
                feature = data.getFeature("BOT_JOIN_NOTIFICATION");
            else if (join)
                feature = data.getFeature("USER_JOIN_NOTIFICATION");
            else if (bot)
                feature = data.getFeature("BOT_LEAVE_NOTIFICATION");
            else
                feature = data.getFeature("USER_LEAVE_NOTIFICATION");

            if (feature.isEnabled()) {
                JDA jda = event.getJDA();
//                TextChannel channel = jda.getTextChannelById(settings.getChannel());
//                String message = settings.getMessage();
//                message = DiscordUtils.get(event, message, event);

//                channel.sendMessage(message).queue();
            }
        }
    }
}
