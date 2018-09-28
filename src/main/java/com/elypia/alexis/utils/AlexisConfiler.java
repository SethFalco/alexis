package com.elypia.alexis.utils;

import com.elypia.alexis.*;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.GuildSettings;
import com.elypia.commandler.jda.JDAConfiler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.Map;

public class AlexisConfiler extends JDAConfiler {

    public AlexisConfiler(String prefix) {
        super(prefix);
    }

    @Override
    public String[] getPrefixes(GenericMessageEvent event) {
        String id = event.getJDA().getSelfUser().getId();
        String defaultPrefix = Alexis.config.getDiscordConfig().getPrefix();

        if (!event.getChannelType().isGuild() || !BotUtils.isDatabaseAlive())
            return new String[]{defaultPrefix, "<@" + id + ">", "<@!" + id + ">"};

        Guild guild = event.getGuild();
        var data = GuildData.query(guild.getIdLong());
        GuildSettings settings = data.getSettings();
        String prefix = settings.getPrefix();

        return new String[]{(prefix == null ? defaultPrefix : prefix), "<@" + id + ">", "<@!" + id + ">"};
    }

    @Override
    public String getHelpUrl(GenericMessageEvent event) {
        return "https://alexis.elypia.com/";
    }

    @Override
    public <T> String getScript(GenericMessageEvent event, String key, Map<String, T> params) {
        String script = BotUtils.getScript(key, event, params);
        return script != null ? script : key;
    }
}
