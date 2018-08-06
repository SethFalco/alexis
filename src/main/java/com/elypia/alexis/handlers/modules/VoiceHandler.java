package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Scope;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import com.elypia.commandler.jda.*;
import com.elypia.elyscript.ElyScript;
import net.dv8tion.jda.core.entities.*;

import java.util.*;
import java.util.stream.Collectors;

@Module(name = "Guild Voice Channels", aliases = {"voice", "vc"}, help = "Convenience commands for to perform actions against voice channels.")
public class VoiceHandler extends JDAHandler {

    private static final ElyScript ALONE_MENTION = new ElyScript("(But... you're|You're) the only (user|one) in ($total_channels ? 1: there : those)?( ^-^'){?}( Aren't you?){?}");
    private static final ElyScript EMPTY_MENTION = new ElyScript("(Wait|Huh)... who (should I|am I (meant|supposed) to) be mentioning?");

    @Scope(ChannelType.TEXT)
    @Command(name = "Mention Members in VC", aliases = {"mention", "at"}, help = "Mention all the users in a voice channel.")
    @Param(name = "channel", help = "The channel(s) to mention users from.")
    public String mention(JDACommand event, @Search(SearchScope.LOCAL) VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

        if (users.remove(event.getMessage().getAuthor())) {
            if (users.size() == 0)
                return ALONE_MENTION.compile(Map.of("total_channels", channels.length));
        }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? EMPTY_MENTION.compile() : joiner.toString();
    }
}
