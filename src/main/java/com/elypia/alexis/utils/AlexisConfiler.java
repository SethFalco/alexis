package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.GuildSettings;
import com.elypia.commandler.confiler.DefaultConfiler;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

public class AlexisConfiler extends DefaultConfiler {

    @Override
    public String getPrefix(MessageReceivedEvent event) {
        String def = Config.getConfig("discord").getString("prefix");

        if (event.isFromType(ChannelType.PRIVATE))
            return def;

        Chatbot bot = Alexis.getChatbot();
        Datastore store = bot.getDatastore();
        Guild guild = event.getGuild();

        Query<GuildData> query = store.createQuery(GuildData.class);
        GuildData data = query.filter("guild_id ==", guild.getIdLong()).get();
        GuildSettings settings = data.getSettings();

        if (settings == null)
            return def;

        String prefix = data.getSettings().getPrefix();
        return prefix == null ? Config.getConfig("discord").getString("prefix") : prefix;
    }
}
