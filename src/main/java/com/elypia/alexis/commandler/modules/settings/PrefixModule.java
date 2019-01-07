package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.commandler.validation.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import javax.validation.constraints.Size;

@Module(id = "Prefix", group = "Settings", aliases = "prefix", help = "prefix.h")
public class PrefixModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public PrefixModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

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
