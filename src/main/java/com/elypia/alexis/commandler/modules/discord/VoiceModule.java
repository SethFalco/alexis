package com.elypia.alexis.commandler.modules.discord;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;

import java.util.*;
import java.util.stream.Collectors;

@Module(id = "Voice Channels", group = "Discord", aliases = {"voice", "vc"}, help = "voice.h")
public class VoiceModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public VoiceModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "Mention", aliases = {"mention", "at"}, help = "voice.mention.h")
    @Param(id = "common.channel", help = "voice.mention.p.channel.h")
    public String mention(
        @Channels(ChannelType.TEXT) JDACEvent event,
        @Search(Scope.LOCAL) VoiceChannel[] channels
    ) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream().map(Member::getUser).filter(o -> !o.isBot()).collect(Collectors.toSet());

        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        if (users.remove(source.getMessage().getAuthor())) {
            if (users.size() == 0)
                return scripts.get("voice.mention.alone", Map.of("channels", channels.length));
        }

        StringJoiner joiner = new StringJoiner(" | ");
        users.forEach(o -> joiner.add(o.getAsMention()));

        return joiner.length() == 0 ? scripts.get("voice.mention.empty") : joiner.toString();
    }
}
