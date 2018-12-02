package com.elypia.alexis.handlers.modules.discord;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import net.dv8tion.jda.core.entities.*;

import java.util.*;
import java.util.stream.Collectors;

@Channel(ChannelType.TEXT)
@Module(name = "voice", group = "Discord", aliases = {"voice", "vc"}, help = "voice.h")
public class VoiceHandler extends JDAHandler {

    @Command(name = "voice.mention", aliases = {"mention", "at"}, help = "voice.mention.h")
    @Param(name = "common.channel", help = "voice.mention.p.channel.h")
    public String mention(JDACommand event, @Search(Scope.LOCAL) VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

        if (users.remove(event.getMessage().getAuthor())) {
            if (users.size() == 0)
                return event.getScript("voice.mention.alone", Map.of("channels", channels.length));
        }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? event.getScript("voice.mention.empty") : joiner.toString();
    }
}
