package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.annotations.validation.command.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.annotations.validation.param.Option;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.*;

@Elevated
@Database
@Module(name = "Welcome and Farewell", aliases = {"greeting", "greetings", "welcome"}, description = "Configure where and what is sent when a user or bot joins and leaves the guild.")
public class GreetingModule extends CommandHandler {

    @Command(name = "Set Greeting Message", aliases = "message", help = "Set a greeting messages for the given event and account type.")
    @Param(name = "greeting", help = "The greeting type, either `welcome` or `farewell`.")
    @Param(name = "account", help = "The account type, either `user` or `bot`.")
    @Param(name = "message", help = "The message to send when this event occurs.")
    public String setGreeting(MessageEvent event, @Option({"welcome", "farewell"}) String greeting, @Option({"user", "bot"}) String account, String message) {
        GenericMessageEvent e = event.getMessageEvent();
        Guild guild = e.getGuild();
        TextChannel channel = e.getTextChannel();
        String type = "settings.greetings." + greeting + "." + account + ".";

        Datastore store = Alexis.getChatbot().getDatastore();

        Query<GuildData> query = store.createQuery(GuildData.class);
        query.filter("guild_id", guild.getIdLong());

        UpdateOperations<GuildData> update = store.createUpdateOperations(GuildData.class);

        update.set(type + "message", message);
        update.set(type + "channel", channel.getIdLong());
        update.set(type + "enabled", true);

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);

        store.update(query, update, options);

        return "I've enabled " + account + " " + greeting + " messages on this channel!";
    }

    @Command(name = "Set Greeting Channel", aliases = "channel", help = "Change the channel that you display bot messages without affecting other settings.")
    @Param(name = "channel", help = "The channel you want greeting messages to be sent too.")
    public String setChannel(MessageEvent event, TextChannel channel) {
        if (!channel.canTalk())
            return "I don't think that's a good idea! I can't send messages to that channel.";

        Guild guild = event.getMessageEvent().getGuild();
        long id = channel.getIdLong();

        Datastore store = Alexis.getChatbot().getDatastore();

        Query<GuildData> query = store.createQuery(GuildData.class);
        query.filter("guild_id", guild.getIdLong());

        UpdateOperations<GuildData> update = store.createUpdateOperations(GuildData.class);

        update.set("settings.greetings.welcome.user.channel", id);
        update.set("settings.greetings.welcome.bot.channel", id);
        update.set("settings.greetings.farewell.user.channel", id);
        update.set("settings.greetings.farewell.bot.channel", id);

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);

        store.update(query, update, options);

        return "I've set the channel for all messages to go through to " + channel.getAsMention() + ".";
    }
}
