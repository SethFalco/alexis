package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.*;
import com.elypia.elyscript.ElyScript;
import net.dv8tion.jda.core.entities.ChannelType;

@Elevated
@Database
@Scope(ChannelType.TEXT)
@Module(name = "Prefix Configuration", aliases = "prefix", help = "Configure your prefix or for big guilds set Alexis for mention only!")
public class PrefixHandler extends JDAHandler {

    private static ElyScript MENTION_ONLY = new ElyScript("(Now ){?}I'll only (respond|be responding) to messages (if|(as|so) long as) they (start|begin) with a mention (at|to) me!");
    private static ElyScript PREFIX_CHANGE = new ElyScript("(Your|This) guild's prefix(, (in order ){?}to perform (my ){?}commands( here){?},){?} has been (set|configured) to: `($guild.prefix)`!");

    @Command(name = "Mention Only", aliases = {"mention", "mentiononly"}, help = "Only trigger Alexis on mention with no other prefix.")
    public String mentionOnly(JDACommand event) {
        String mention = event.getSource().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return MENTION_ONLY.compile();
    }

    @Default
    @Command(name = "Set Prefix", aliases = {"set", "prefix"}, help = "Change the prefix of the bot for this guild only.")
    @Param(name = "prefix", help = "The new prefix you want to set.")
    public String setPrefix(JDACommand event, @Length(min = 1, max = 32) String prefix) {
        long id = event.getSource().getGuild().getIdLong();
        GuildData data = GuildData.query(id);
        data.getSettings().setPrefix(prefix);
        data.commit();

        return BotUtils.buildScript(null, event.getSource());
    }
}
