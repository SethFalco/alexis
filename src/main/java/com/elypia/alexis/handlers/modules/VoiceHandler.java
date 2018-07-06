package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.elyscript.ElyScript;
import net.dv8tion.jda.core.entities.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.elypia.commandler.data.SearchScope.LOCAL;

@Module(name = "Guild Voice Channels", aliases = {"voice", "vc"}, description = "Convenience commands for to perform actions against voice channels.")
public class VoiceHandler extends CommandHandler {

    private static final ElyScript ALONE_MENTION = new ElyScript("(But... you're|You're) the only (user|one) in %s?( ^-^'){?}( Aren't you?){?}");
    private static final ElyScript EMPTY_MENTION = new ElyScript("(Wait|Huh)... who (should I|am I (meant|supposed) to) be mentioning?");

    @Scope(ChannelType.TEXT)
    @Command(name = "Mention Members in VC", aliases = {"mention", "at"}, help = "Mention all the users in a voice channel.")
    @Param(name = "channel", help = "The channel(s) to mention users from.")
    public String mention(AbstractEvent event, @Search(LOCAL) VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

        if (users.remove(event.getMessage().getAuthor())) {
            if (users.size() == 0) {
                String there = channels.length == 1 ? "there" : "those";
                return ALONE_MENTION.compile(there);
            }
        }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? EMPTY_MENTION.compile() : joiner.toString();
    }
}
