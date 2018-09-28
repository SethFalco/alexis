package com.elypia.alexis.handlers.modules.settings;

import com.elypia.alexis.commandler.validators.Supported;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import com.elypia.elypiai.utils.Language;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

@Module(name = "lang.title", group = "Settings", aliases = {"language", "languages", "lang"}, help = "lang.help")
public class LanguageHandler extends JDAHandler {

    @Channel(ChannelType.TEXT)
    @Command(name = "lang.global.title", aliases = "global", help = "lang.global.help")
    @Param(name = "common.lang", help = "lang.param.lang.help")
    public String setGlobalLanguage(JDACommand event, @Supported Language language) {
        List<TextChannel> channels = event.getMessage().getGuild().getTextChannels();
        setLanguages(language, channels.toArray(new MessageChannel[0]));

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , false
        );

        return BotUtils.getScript("global.lang.changed", event.getSource(), params);
    }

    @Command(name = "lang.local.title", aliases = "local", help = "lang.local.help")
    @Param(name = "common.lang", help = "lang.param.lang.help")
    public String setLocalLanguage(JDACommand event, @Supported Language language) {
        MessageChannel channel = event.getMessage().getChannel();
        setLanguages(language, channel);

        boolean isPrivate = !channel.getType().isGuild();

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , isPrivate
        );

        return event.getScript("local.lang.changed", params);
    }

    private void setLanguages(Language language, MessageChannel... channels) {
        String code = language.getCode();

        for (MessageChannel channel : channels) {
            long id = channel.getIdLong();

            MessageChannelData data = MessageChannelData.query(id);
            data.setLanguage(code);
            data.commit();
        }
    }
}
