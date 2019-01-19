package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.TranslateSettings;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.Map;

@Module(id = "Translate", group = "Settings", aliases = {"translate", "trans", "tran"}, help = "translate.h")
public class TranslateModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public TranslateModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "translate.toggle", aliases = "toggle", help = "translate.toggle.h")
    @Param(id = "common.toggle", help = "translate.toggle.p.toggle.h")
    public String toggle(@Channels(ChannelType.TEXT) @Elevated JDACEvent event, boolean toggle) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isEnabled() == toggle)
            return scripts.get("translate.toggle.no_change", Map.of("enabled", toggle));

        settings.setEnabled(toggle);
        data.commit();

        return scripts.get("translate.toggle.response", Map.of("enabled", toggle));
    }

    @Command(id = "translate.dm", aliases = "private", help = "translate.dm.h")
    @Param(id = "common.toggle", help = "translate.dm.p.toggle.h")
    public String inPrivate(@Channels(ChannelType.TEXT) @Elevated JDACEvent event, boolean isPrivate) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isPrivate() == isPrivate)
            return scripts.get("translate.dm.no_change", Map.of("enabled", isPrivate));

        settings.setPrivate(isPrivate);
        data.commit();

        return scripts.get("translate.dm.response", Map.of("enabled", isPrivate));
    }
}
