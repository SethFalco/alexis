package com.elypia.alexis.handlers.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.MessageSettings;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

@Channel(ChannelType.TEXT)
@Module(name = "level.title", group = "Settings", aliases = {"level", "lvl"}, help = "level.help")
public class LevelHandler extends JDAHandler {

    @Command(name = "level.message.name", aliases = "message", help = "level.message.help")
    @Param(name = "level.param.message.help", help = "level.message.message.help")
    public void setMessage(JDACommand event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        settings.setEnabled(true);
        settings.setChannel(event.getMessage().getChannel().getIdLong());
        settings.setMessage(message);

        data.commit();

        event.reply(BotUtils.getScript("level.message.response", event.getSource()));
    }

    @Command(name = "level.test.name", aliases = "test", help = "level.test.help")
    public String test(JDACommand event) {
        GenericMessageEvent source = event.getSource();
        long id = source.getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        String message = settings.getMessage();

        if (message == null || message.isEmpty())
            return BotUtils.getScript("level.no_message", source);

        return BotUtils.getScript(message, source);
    }
}
