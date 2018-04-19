package com.elypia.alexis.discord.handlers.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.annotations.access.Scope;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.util.List;

@Module(
    aliases = {"emote", "emoji", "emoticon"},
    help = "Check all the emotes or how much they're being used."
)
public class EmoteHandler extends CommandHandler {

    @CommandGroup("list")
    @Command(aliases = "list", help = "List all of the custom emotes in this guild.")
    @Scope(ChannelType.TEXT)
    public void listEmotes(MessageEvent event) {
        listEmotes(event, event.getMessageEvent().getGuild());
    }

    @CommandGroup("list")
    @Param(name = "guild", help = "The guild to get emotes from.")
    @Scope(ChannelType.PRIVATE)
    public void listEmotes(MessageEvent event, Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0) {
            event.reply("You don't actually have any emotes though... ^-^'");
            return;
        }

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        event.reply(builder.toString());
    }

    @CommandGroup("post")
    @Command(aliases = {"get", "post"}, help = "Post an emote in the chat!")
    @Param(name = "emote", help = "A way to identify the emote you want to post.")
    public void post(MessageEvent event, Emote emote) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setImage(emote.getImageUrl());
        event.reply(builder);
    }
}
