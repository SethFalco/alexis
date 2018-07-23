package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.MessageSettings;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.Scope;
import net.dv8tion.jda.core.entities.ChannelType;

@Scope(ChannelType.TEXT)
@Module(name = "Level Settings", aliases = {"level", "lvl"}, help = "Configure level up messages or notifications.")
public class LevelHandler extends JDAHandler {

    public String setMessage(JDACommand event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        settings.setEnabled(true);
        settings.setChannel(event.getMessage().getChannel().getIdLong());
        settings.setMessage(message);

        data.commit();
        return "I've enabled level up messages for this guild now!";
    }

    public String test(JDACommand event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();

        return BotUtils.getScript(settings.getMessage(), event.getSource());
    }
}
