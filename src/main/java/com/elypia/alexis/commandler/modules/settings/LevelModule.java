package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.MessageSettings;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

@Module(id = "Levels", group = "Settings", aliases = {"level", "lvl"}, help = "level.help")
public class LevelModule extends JDACHandler {

    @Command(id = "level.message.name", aliases = "message", help = "level.message.help")
    @Param(id = "level.param.message.help", help = "level.message.message.help")
    public String setMessage(@Channels(ChannelType.TEXT) JDACEvent event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        settings.setEnabled(true);
        settings.setChannel(event.asMessageRecieved().getChannel().getIdLong());
        settings.setMessage(message);

        data.commit();

        return scripts.get(event.getSource(), "level.message.response");
    }

    @Command(id = "level.test.name", aliases = "test", help = "level.test.help")
    public String test(@Channels(ChannelType.TEXT) JDACEvent event) {
        GenericMessageEvent source = event.getSource();
        long id = source.getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        String message = settings.getMessage();

        if (message == null || message.isEmpty())
            return scripts.get(source, "level.no_message");

        return scripts.get(source, message);
    }
}
