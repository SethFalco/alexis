package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.MessageSettings;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Scope;
import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

@Scope(ChannelType.TEXT)
@Module(name = "level.title", aliases = {"level", "lvl"}, help = "level.help")
public class LevelHandler extends JDAHandler {

    @Command(name = "level.message.title", aliases = "message", help = "level.message.help")
    @Param(name = "message", help = "level.message.message.help")
    public void setMessage(JDACommand event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        settings.setEnabled(true);
        settings.setChannel(event.getMessage().getChannel().getIdLong());
        settings.setMessage(message);

        data.commit();

        event.reply(BotUtils.getScript("level.message.response", event.getSource()));
        event.trigger("level test");
    }

    @Command(name = "level.test.title", aliases = "test", help = "level.test.help")
    public String test(JDACommand event) {
        GenericMessageEvent source = event.getSource();
        long id = source.getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        String message = settings.getMessage();

        if (message == null || message.isEmpty())
            return BotUtils.getScript("level.test.no_message", source);

        return BotUtils.getScript(message, source);
    }
}
