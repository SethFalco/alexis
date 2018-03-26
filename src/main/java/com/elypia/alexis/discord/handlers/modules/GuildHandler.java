package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.annotations.*;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.util.Collection;

@Module(
    aliases = "guild",
    help = "Guild related commands to perform actions or get info."
)
public class GuildHandler extends CommandHandler {

    @CommandGroup("info")
    @Command(aliases = "info", help = "Get the guilds information.")
    @Scope(ChannelType.TEXT)
    public void info(MessageEvent event) {
        info(event, event.getGuild());
    }

    @CommandGroup("info")
    @Parameter(name = "guild", help = "Some form of identification for this guild.")
    @Scope(ChannelType.PRIVATE)
    public void info(MessageEvent event, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField("Owner", guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField("Users (Bots)", memberField, true);

        event.reply(builder);
    }

    @CommandGroup("prune")
    @Command(aliases = "prune", help = "Delete messages in the guild in bulk.")
    @Parameter(name = "count", help = "The number of messages to delete.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public void prune(MessageEvent event, int count) {
        prune(event, count, event.getMessage().getTextChannel());
    }

    @CommandGroup("prune")
    @Parameter(name = "count", help = "The number of messages to delete.")
    @Parameter(name = "channel", help = "The channel to delete messages in.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public void prune(MessageEvent event, int count, TextChannel channel) {
        if (count < 2 || count > 100) {
            event.reply("Please specify between 2 and 100 messages.");
            return;
        }

        channel.getHistoryBefore(event.getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.tryDeleteMessage();
            });
        });
    }
}
