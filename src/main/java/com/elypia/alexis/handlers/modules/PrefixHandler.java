package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.events.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.elyscript.ElyScript;
import net.dv8tion.jda.core.entities.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;

@Elevated
@Database
@Scope(ChannelType.TEXT)
@Module(name = "Prefix Configuration", aliases = "prefix", description = "Configure your prefix or for big guilds set Alexis for mention only!")
public class PrefixHandler extends CommandHandler {

    private static ElyScript MENTION_ONLY = new ElyScript("(Now ){?}I'll only (respond|be responding) to messages (if|(as|so) long as) they (start|begin) with a mention (at|to) me!");
    private static ElyScript PREFIX_CHANGE = new ElyScript("(Your|This) guild's prefix(, (in order ){?}to perform (my ){?}commands( here){?},){?} has been (set|configured) to: `%s`!");

    @Command(name = "Mention Only", aliases = {"mention", "mentiononly"}, help = "Only trigger Alexis on mention with no other prefix.")
    public String mentionOnly(AbstractEvent event) {
        String mention = event.getMessageEvent().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return MENTION_ONLY.compile();
    }

    @Default
    @Command(name = "Set Prefix", aliases = {"set", "prefix"}, help = "Change the prefix of the bot for this guild only.")
    @Param(name = "prefix", help = "The new prefix you want to set.")
    public String setPrefix(AbstractEvent event, @Length(min = 1, max = 32) String prefix) {
        Datastore store = Alexis.getChatbot().getDatastore();
        Guild guild = event.getMessageEvent().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        UpdateOperations<GuildData> update = store.createUpdateOperations(GuildData.class);

        update.set("settings.prefix", prefix);
        store.update(data, update);

        return PREFIX_CHANGE.compile(prefix);
    }
}
