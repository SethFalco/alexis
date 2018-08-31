package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.Collection;

@Module(name = "guild.title", aliases = "guild", help = "guild.help")
public class GuildHandler extends JDAHandler {

    @Command(id = 8, name = "guild.info.title", aliases = "info", help = "guild.info.help")
    @Scope(ChannelType.TEXT)
    public void info(JDACommand event) {
        info(event, event.getSource().getGuild());
    }

    @Overload(8)
    @Param(name = "guild", help = "guild.info.guild.help")
    @Scope(ChannelType.PRIVATE)
    public EmbedBuilder info(JDACommand event, Guild guild) {
        GenericMessageEvent source = event.getSource();
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event.getSource().getGuild());

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField(BotUtils.getScript("guild.info.owner", source), guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(BotUtils.getScript("guild.info.users", source), memberField, true);

        return builder;
    }

    @Scope(ChannelType.TEXT)
    @Permissions(Permission.MANAGE_SERVER)
    @Command(id = 9, name = "guild.prune.title", aliases = "prune", help = "guild.prune.help")
    @Param(name = "count", help = "guild.prune.count.help")
    public void prune(JDACommand event, @Limit(min = 2, max = 100) int count) {
        prune(event, count, event.getSource().getTextChannel());
    }

    @Permissions(Permission.MANAGE_SERVER)
    @Scope(ChannelType.TEXT)
    @Overload(9)
    @Param(name = "channel", help = "guild.prune.channel.help")
    public String prune(JDACommand event, int count, TextChannel channel) {
        channel.getHistoryBefore(event.getMessage(), count).queue(o -> {
            channel.deleteMessages(o.getRetrievedHistory()).queue(ob -> {
                event.deleteMessage();
            });
        });

        return null;
    }
}
