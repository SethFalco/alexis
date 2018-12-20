package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

import javax.validation.constraints.*;
import java.util.Collection;

@Module(id = "Guilds", group = "Discord", aliases = "guild", help = "guild.help")
public class GuildModule extends JDACHandler {

    @Command(id = "Guild Info", aliases = "info", help = "guild.info.help")
    public void info(@Channels(ChannelType.TEXT) JDACEvent event) {
        info(event, event.getSource().getGuild());
    }

    @Overload("Guild Info")
    @Param(id = "common.guild", help = "guild.param.guild.help")
    public EmbedBuilder info(
        @Channels(ChannelType.PRIVATE) JDACEvent event,
        @Search(Scope.MUTUAL) Guild guild
    ) {
        GenericMessageEvent source = event.getSource();
        EmbedBuilder builder = BotUtils.newEmbed(event.getSource().getGuild());

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField(scripts.get(source, "guild.owner"), guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(scripts.get(source, "guild.users"), memberField, true);

        return builder;
    }

    @Command(id = "Prune Messages", aliases = "prune", help = "guild.prune.help")
    @Param(id = "count", help = "guild.param.count.help")
    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) JDACEvent event,
        @Min(2) @Max(100) int count
    ) {
        prune(event, count, event.getSource().getTextChannel());
    }

    @Overload("Prune Messages")
    @Param(id = "common.channel", help = "guild.param.channel.help")
    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) JDACEvent event,
        @Min(2) @Max(100) int count,
        TextChannel channel
    ) {
        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        channel.getHistoryBefore(source.getMessageIdLong(), count).queue(history -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue(command ->
                source.getMessage().delete().queue()
            );
        });
    }
}
