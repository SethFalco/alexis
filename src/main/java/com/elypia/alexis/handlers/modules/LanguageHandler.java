package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.Supported;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Scope;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.utils.Language;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

@Module(name = "language.title", aliases = {"language", "languages", "lang"}, help = "language.help")
public class LanguageHandler extends JDAHandler {

    @Scope(ChannelType.TEXT)
    @Command(name = "language.global.title", aliases = "global", help = "language.global.help")
    @Param(name = "language.param.language.name", help = "language.param.language.help")
    public String setGlobalLanguage(JDACommand event, @Supported Language language) {
        List<TextChannel> channels = event.getMessage().getGuild().getTextChannels();
        setLanguages(language, channels.toArray(new MessageChannel[0]));

        Map<String, Object> params = Map.of("language", language.getLanguageName());
        return BotUtils.getScript("global.language.changed", event.getSource(), params);
    }

    @Command(name = "language.local.title", aliases = "local", help = "language.local.help")
    @Param(name = "language.param.language.name", help = "language.param.language.help")
    public String setLocalLanguage(JDACommand event, @Supported Language language) {
        MessageChannel channel = event.getMessage().getChannel();
        setLanguages(language, channel);

        Map<String, Object> params = Map.of("language", language.getLanguageName());
        return BotUtils.getScript("local.language.changed", event.getSource(), params);
    }

    private void setLanguages(Language language, MessageChannel... channels) {
        for (MessageChannel channel : channels) {
            MessageChannelData data = MessageChannelData.query(channel.getIdLong());
            data.setLanguage(language.getCode());
            data.commit();
        }
    }
}
