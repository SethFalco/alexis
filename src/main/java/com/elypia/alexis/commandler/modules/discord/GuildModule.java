package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.cmdlrdiscord.annotations.Scoped;
import com.elypia.cmdlrdiscord.constraints.*;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.inject.Inject;
import javax.validation.constraints.*;
import java.util.Collection;

@Module(name = "guild", group = "Discord", aliases = "guild")
public class GuildModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public GuildModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "info", aliases = "info")
    public EmbedBuilder info(
        CommandlerEvent<Event> event,
        @Param(name = "guild", defaultValue = "${src.guild.id}") @Scoped Guild guild
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

    @Command(name = "prune", aliases = "prune")
    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) CommandlerEvent<?> event,
        @Param(name = "count") @Min(2) @Max(100) int count,
        @Param(name = "channel", defaultValue = "${src.channel.id}") TextChannel channel
    ) {
        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        channel.getHistoryBefore(source.getMessageIdLong(), count).queue(history -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue(command ->
                source.getMessage().delete().queue()
            );
        });
    }
}
