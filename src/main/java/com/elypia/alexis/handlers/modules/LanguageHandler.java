package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.Supported;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.elypiai.utils.Language;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

@Module(name = "Languages", aliases = {"language", "languages", "lang"}, help = "help.language")
public class LanguageHandler extends JDAHandler {

    @Scope(ChannelType.TEXT)
    @Command(name = "Change Global Language", aliases = "global", help = "help.language.global")
    @Param(name = "language", help = "help.language.global")
    public String setGlobalLanguage(JDACommand event, @Supported Language language) {
        Map<String, Object> params = new HashMap<>();
        params.put("language", language.getLanguageName());

        Guild guild = event.getMessage().getGuild();
//        setLanguages(language, guild.getTextChannels());

        return BotUtils.getScript("language.changed", event.getSource(), params);
    }

    @Command(name = "Change Local Language", aliases = "local", help = "help.language.local")
    @Param(name = "language", help = "help.language.global")
    public String setLocalLanguage(JDACommand event, @Supported Language language) {
        Map<String, Object> params = new HashMap<>();
        params.put("language", language.getLanguageName());

        MessageChannel channel = event.getMessage().getChannel();
        setLanguages(language, channel);

        return BotUtils.getScript("language.changed", event.getSource(), params);
    }

    private <T extends MessageChannel> void setLanguages(Language language, T... channels) {
        String code = language.getCode();

        for (MessageChannel channel : channels) {
            MessageChannelData data = MessageChannelData.query(channel.getIdLong());
            data.setLanguage(code);
            data.commit();
        }
    }
}
