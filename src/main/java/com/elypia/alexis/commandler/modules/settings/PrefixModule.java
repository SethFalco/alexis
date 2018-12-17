package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.commandler.validation.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.core.entities.ChannelType;

import javax.validation.constraints.Size;

@Module(id = "Prefix", group = "Settings", aliases = "prefix", help = "prefix.h")
public class PrefixModule extends JDACHandler {

    @Command(id = "prefix.mention", aliases = {"mention", "mentiononly"}, help = "prefix.mention.h")
    public String mentionOnly(
        @Channels(ChannelType.TEXT) @Elevated @Database JDACEvent event
    ) {
        String mention = event.getSource().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return scripts.get("prefix.mention.response");
    }

    @Default
    @Command(id = "prefix.change", aliases = {"set", "prefix"}, help = "prefix.change.h")
    @Param(id = "prefix.change.p.prefix", help = "prefix.change.p.prefix.h")
    public String setPrefix(
        @Channels(ChannelType.TEXT) @Elevated @Database JDACEvent event,
        @Size(min = 1, max = 32) String prefix
    ) {
        long id = event.getSource().getGuild().getIdLong();
        GuildData data = GuildData.query(id);
        data.getSettings().setPrefix(prefix);
        data.commit();

        return scripts.get("prefix.change.response");
    }
}
