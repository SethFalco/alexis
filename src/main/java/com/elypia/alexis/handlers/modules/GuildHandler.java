package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.Limit;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.Collection;

@Module(name = "Guild", aliases = "guild", help = "Guild related commands to perform actions or get info.")
public class GuildHandler extends JDAHandler {

    @Command(id = 8, name = "Guild Info", aliases = "info", help = "Get the guilds information.")
    @Scope(ChannelType.TEXT)
    public void info(JDACommand event) {
        info(event, event.getSource().getGuild());
    }

    @Overload(8)
    @Param(name = "guild", help = "Some form of identification for this guild.")
    @Scope(ChannelType.PRIVATE)
    public EmbedBuilder info(JDACommand event, Guild guild) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event.getSource().getGuild());

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField("Owner", guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField("Users (Bots)", memberField, true);

        return builder;
    }

    @Command(id = 9, name = "Delete Messages", aliases = "prune", help = "Delete messages in the guild in bulk.")
    @Param(name = "count", help = "The number of messages to delete.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public void prune(JDACommand event, int count) {
        prune(event, count, event.getSource().getTextChannel());
    }

    @Overload(9)
    @Param(name = "channel", help = "The channel to delete messages in.")
    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    public String prune(JDACommand event, @Limit(min = 2, max = 100) int count, TextChannel channel) {
        channel.getHistoryBefore(event.getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.deleteMessage();
            });
        });

        return null;
    }
}
