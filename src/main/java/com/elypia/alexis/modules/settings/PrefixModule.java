package com.elypia.alexis.modules.settings;

import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.*;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.core.entities.ChannelType;

@Elevated
@Database
@Channel(ChannelType.TEXT)
@Module(name = "prefix", group = "Settings", aliases = "prefix", help = "prefix.h")
public class PrefixModule extends JDACHandler {

    @Command(name = "prefix.mention", aliases = {"mention", "mentiononly"}, help = "prefix.mention.h")
    public String mentionOnly(JDACommand event) {
        String mention = event.getSource().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return event.getScript("prefix.mention.response");
    }

    @Default
    @Command(name = "prefix.change", aliases = {"set", "prefix"}, help = "prefix.change.h")
    @Param(name = "prefix.change.p.prefix", help = "prefix.change.p.prefix.h")
    public String setPrefix(JDACommand event, @Length(min = 1, max = 32) String prefix) {
        long id = event.getSource().getGuild().getIdLong();
        GuildData data = GuildData.query(id);
        data.getSettings().setPrefix(prefix);
        data.commit();

        return event.getScript("prefix.change.response");
    }
}
