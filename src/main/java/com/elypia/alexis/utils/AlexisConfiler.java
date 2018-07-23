package com.elypia.alexis.utils;

import com.elypia.alexis.*;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.GuildSettings;
import com.elypia.commandler.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

public class AlexisConfiler extends JDAConfiler {

    public AlexisConfiler(String prefix) {
        super(prefix);
    }

    @Override
    public String[] getPrefixes(Commandler<JDA, GenericMessageEvent, Message> commandler, GenericMessageEvent event) {
        String id = commandler.getClient().getSelfUser().getId();
        String defaultPrefix = Alexis.getConfig().getDiscordConfig().getPrefix();

        if (event.isFromType(ChannelType.PRIVATE) || !BotUtils.isDatabaseAlive())
            return new String[]{defaultPrefix, "<@" + id + ">", "<@!" + id + ">"};

        Chatbot bot = Alexis.getChatbot();
        Datastore store = bot.getDatastore();
        Guild guild = event.getGuild();

        Query<GuildData> query = store.createQuery(GuildData.class);
        GuildData data = query.filter("guild_id ==", guild.getIdLong()).get();
        GuildSettings settings = data.getSettings();

        String prefix = settings.getPrefix();
        return new String[]{(prefix == null ? defaultPrefix : prefix), "<@" + id + ">", "<@!" + id + ">"};
    }

    @Override
    public String getHelpUrl(Commandler<JDA, GenericMessageEvent, Message> commandler, GenericMessageEvent event) {
        return "https://alexis.elypia.com/";
    }

    @Override
    public String getHelp(Commandler<JDA, GenericMessageEvent, Message> commandler, GenericMessageEvent event, String key) {
        return BotUtils.getScript(key, event);
    }
}
