package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.access.Scope;
import com.elypia.commandler.annotations.validation.Length;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.entities.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;

@Scope(ChannelType.TEXT)
@Module(name = "Prefix Configuration", aliases = "prefix", description = "Configure your prefix or for big guilds set Alexis for mention only!")
public class PrefixHandler extends CommandHandler {

    @Command(aliases = {"mention", "mentiononly"}, help = "Only trigger Alexis on mention with no other prefix.")
    public String mentionOnly(MessageEvent event) {
        String mention = event.getMessageEvent().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return "Now the bot can only be used through mentions, there is no other prefix!";
    }

    @Default
    @Command(aliases = {"set", "prefix"}, help = "Change the prefix of the bot for this guild only.")
    @Param(name = "prefix", help = "The new prefix you want to set.")
    public String setPrefix(MessageEvent event, @Length(min = 1, max = 32) String prefix) {
        Datastore store = Alexis.getChatbot().getDatastore();
        Guild guild = event.getMessageEvent().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        UpdateOperations<GuildData> update = store.createUpdateOperations(GuildData.class);

        update.set("settings.prefix", prefix);
        store.update(data, update);

        return "You guilds prefix has now been set to: `" + prefix + "`!";
    }
}
