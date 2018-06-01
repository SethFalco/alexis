package com.elypia.alexis.discord.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.CommandGroup;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.access.Permissions;
import com.elypia.commandler.annotations.access.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.util.Collection;

@Module(
    name = "Guild / Server",
    aliases = "guild",
    description = "Guild related commands to perform actions or get info."
)
public class GuildHandler extends CommandHandler {

    @CommandGroup("info")
    @Command(aliases = "info", help = "Get the guilds information.")
    @Scope(ChannelType.TEXT)
    public void info(MessageEvent event) {
        info(event, event.getMessageEvent().getGuild());
    }

    @CommandGroup("info")
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

    @CommandGroup("prune")
    @Command(aliases = "prune", help = "Delete messages in the guild in bulk.")
    @Param(name = "count", help = "The number of messages to delete.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public void prune(MessageEvent event, int count) {
        prune(event, count, event.getMessageEvent().getTextChannel());
    }

    @CommandGroup("prune")
    @Param(name = "count", help = "The number of messages to delete.")
    @Param(name = "channel", help = "The channel to delete messages in.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public String prune(MessageEvent event, int count, TextChannel channel) {
        if (count < 2 || count > 100)
            return "Please specify between 2 and 100 messages.";

        channel.getHistoryBefore(event.getMessageEvent().getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.tryDeleteMessage();
            });
        });

        return null;
    }
}
