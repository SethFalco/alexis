package com.elypia.alexis.modules.discord;

import com.elypia.cmdlrdiscord.Scope;
import com.elypia.cmdlrdiscord.annotations.Scoped;
import com.elypia.cmdlrdiscord.constraints.Channels;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Module(name = "voice", group = "Discord", aliases = {"voice", "vc"}, help = "voice.h")
public class VoiceModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public VoiceModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "voice.mention", aliases = {"mention", "at"}, help = "voice.mention.h")
    public String mention(
        @Channels(ChannelType.TEXT) CommandlerEvent<?> event,
        @Param(name = "channel", help = "voice.mention.channel") @Scoped(inGuild = Scope.LOCAL) VoiceChannel[] channels
    ) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        if (users.remove(source.getMessage().getAuthor())) {
            if (users.size() == 0)
                return lang.get("voice.mention.alone", Map.of("channels", channels.length));
        }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? lang.get("voice.mention.empty") : joiner.toString();
    }
}
