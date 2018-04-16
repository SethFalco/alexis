package com.elypia.alexis.discord.handlers.modules;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.command.Command;
import com.elypia.commandler.annotations.command.Module;
import com.elypia.commandler.annotations.command.Param;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;

@Module(
    aliases = {"voice", "vc"},
    help = "Convenience commands for voice channels."
)
public class VoiceHandler extends CommandHandler {

    @Command(aliases = {"mention", "at"}, help = "Mention all the users in a voice channel.")
    @Param(name = "channel", help = "The channel or channels to mention users from.")
    public void mention(MessageEvent event, VoiceChannel[] channel) {
        Set<Member> members = new HashSet<>();
        StringJoiner joiner = new StringJoiner(" ");
        Arrays.stream(channel).filter(Objects::nonNull).forEach(o -> members.addAll(o.getMembers()));

        for (Member member : members) {
            if (!member.getUser().isBot())
                joiner.add(member.getAsMention());
        }

        if (joiner.length() == 0)
            event.reply("Wait... who should I be mentioning?");
        else
            event.reply(joiner.toString());
    }
}
