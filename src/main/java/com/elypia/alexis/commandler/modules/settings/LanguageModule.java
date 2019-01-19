package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.commandler.validation.Supported;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.Language;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.*;

@Module(id = "Language", group = "Settings", aliases = {"language", "languages", "lang"}, help = "lang.help")
public class LanguageModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public LanguageModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "lang.global.title", aliases = "global", help = "lang.global.help")
    @Param(id = "common.lang", help = "lang.param.lang.help")
    public String setGlobalLanguage(@Channels(ChannelType.TEXT) JDACEvent event, @Supported Language language) {
        List<TextChannel> channels = event.asMessageRecieved().getGuild().getTextChannels();
        setLanguages(language, channels.toArray(new MessageChannel[0]));

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , false
        );

        return scripts.get(event.getSource(), "global.lang.changed", params);
    }

    @Command(id = "lang.local.title", aliases = "local", help = "lang.local.help")
    @Param(id = "common.lang", help = "lang.param.lang.help")
    public String setLocalLanguage(JDACEvent event, @Supported Language language) {
        MessageChannel channel = event.asMessageRecieved().getChannel();
        setLanguages(language, channel);

        boolean isPrivate = !channel.getType().isGuild();

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , isPrivate
        );

        return scripts.get("local.lang.changed", params);
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
