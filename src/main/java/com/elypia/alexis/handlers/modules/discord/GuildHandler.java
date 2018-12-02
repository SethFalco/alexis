package com.elypia.alexis.handlers.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.*;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.Collection;

@Module(name = "guild.title", group = "Discord", aliases = "guild", help = "guild.help")
public class GuildHandler extends JDAHandler {

    @Command(id = 8, name = "guild.info.name", aliases = "info", help = "guild.info.help")
    @Channel(ChannelType.TEXT)
    public void info(JDACommand event) {
        info(event, event.getSource().getGuild());
    }

    @Overload(8)
    @Param(name = "common.guild", help = "guild.param.guild.help")
    @Channel(ChannelType.PRIVATE)
    public EmbedBuilder info(JDACommand event, @Search(Scope.MUTUAL) Guild guild) {
        GenericMessageEvent source = event.getSource();
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event.getSource().getGuild());

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField(BotUtils.getScript("guild.owner", source), guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(BotUtils.getScript("guild.users", source), memberField, true);

        return builder;
    }

    @Channel(ChannelType.TEXT)
    @Permissions(Permission.MANAGE_SERVER)
    @Command(id = 9, name = "guild.prune.name", aliases = "prune", help = "guild.prune.help")
    @Param(name = "count", help = "guild.param.count.help")
    public void prune(JDACommand event, @Limit(min = 2, max = 100) int count) {
        prune(event, count, event.getSource().getTextChannel());
    }

    @Overload(9)
    @Param(name = "common.channel", help = "guild.param.channel.help")
    public String prune(JDACommand event, int count, TextChannel channel) {
        channel.getHistoryBefore(event.getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.deleteMessage();
            });
        });

        return null;
    }
}
