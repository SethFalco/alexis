package com.elypia.alexis.utils;

import com.elypia.alexis.*;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.GuildSettings;
import com.elypia.commandler.confiler.DefaultConfiler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

public class AlexisConfiler extends DefaultConfiler {

    @Override
    public String getPrefix(GenericMessageEvent event) {
        String defaultPrefix = Alexis.getConfig().getDiscordConfig().getPrefix();

        if (event.isFromType(ChannelType.PRIVATE) || !BotUtils.isDatabaseAlive())
            return defaultPrefix;

        Chatbot bot = Alexis.getChatbot();
        Datastore store = bot.getDatastore();
        Guild guild = event.getGuild();

        Query<GuildData> query = store.createQuery(GuildData.class);
        GuildData data = query.filter("guild_id ==", guild.getIdLong()).get();
        GuildSettings settings = data.getSettings();

        String prefix = settings.getPrefix();
        return prefix == null ? defaultPrefix : prefix;
    }
}
