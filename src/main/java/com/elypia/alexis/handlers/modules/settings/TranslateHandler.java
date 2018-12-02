package com.elypia.alexis.handlers.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.TranslateSettings;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.*;
import net.dv8tion.jda.core.entities.*;

import java.util.Map;

@Elevated
@Channel(ChannelType.TEXT)
@Module(name = "translate", group = "Settings", aliases = {"translate", "trans", "tran"}, help = "translate.h")
public class TranslateHandler extends JDAHandler {

    @Channel(ChannelType.TEXT)
    @Command(name = "translate.toggle", aliases = "toggle", help = "translate.toggle.h")
    @Param(name = "common.toggle", help = "translate.toggle.p.toggle.h")
    public String toggle(JDACommand event, boolean toggle) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isEnabled() == toggle)
            return event.getScript("translate.toggle.no_change", Map.of("enabled", toggle));

        settings.setEnabled(toggle);
        data.commit();

        return event.getScript("translate.toggle.response", Map.of("enabled", toggle));
    }

    @Command(name = "translate.dm", aliases = "private", help = "translate.dm.h")
    @Param(name = "common.toggle", help = "translate.dm.p.toggle.h")
    public String inPrivate(JDACommand event, boolean isPrivate) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isPrivate() == isPrivate)
            return event.getScript("translate.dm.no_change", Map.of("enabled", isPrivate));

        settings.setPrivate(isPrivate);
        data.commit();

        return event.getScript("translate.dm.response", Map.of("enabled", isPrivate));
    }
}
