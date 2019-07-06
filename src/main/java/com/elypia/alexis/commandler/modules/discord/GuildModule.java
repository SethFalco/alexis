package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.commandler.dyndefault.CurrentGuild;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.discord.annotations.Scoped;
import com.elypia.commandler.discord.constraints.Channels;
import com.elypia.commandler.interfaces.*;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.inject.Inject;
import javax.validation.constraints.*;
import java.util.Collection;

@Module(name = "Guilds", group = "Discord", aliases = "guild", help = "Manage and automate your guild.")
public class GuildModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public GuildModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "Guild Info", aliases = "info", help = "Get the guild's information.")
    public EmbedBuilder info(
        CommandlerEvent<Event> event,
        @Param(name = "guild", help = "A mutual guild by name or ID.", dynDefaultValue = CurrentGuild.class) @Scoped Guild guild
    ) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField(lang.get(event, "guild.owner"), guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(lang.get(event, "guild.users"), memberField, true);

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
