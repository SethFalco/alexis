package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.Collection;

@Module(name = "Guild", aliases = "guild", description = "Guild related commands to perform actions or get info.")
public class GuildHandler extends CommandHandler {

    @Overload("info")
    @Command(name = "Guild Info", aliases = "info", help = "Get the guilds information.")
    @Scope(ChannelType.TEXT)
    public void info(MessageEvent event) {
        info(event, event.getMessageEvent().getGuild());
    }

    @Overload("info")
    @Param(name = "guild", help = "Some form of identification for this guild.")
    @Scope(ChannelType.PRIVATE)
    public EmbedBuilder info(MessageEvent event, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField("Owner", guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField("Users (Bots)", memberField, true);

        return builder;
    }

    @Overload("prune")
    @Command(name = "Delete Messages", aliases = "prune", help = "Delete messages in the guild in bulk.")
    @Param(name = "count", help = "The number of messages to delete.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public void prune(MessageEvent event, int count) {
        prune(event, count, event.getMessageEvent().getTextChannel());
    }

    @Overload("prune")
    @Param(name = "count", help = "The number of messages to delete.")
    @Param(name = "channel", help = "The channel to delete messages in.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public String prune(MessageEvent event, int count, TextChannel channel) {
        if (count < 2 || count > 100)
            return "Please specify between 2 and 100 messages.";

        channel.getHistoryBefore(event.getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.tryDeleteMessage();
            });
        });

        return null;
    }
}
