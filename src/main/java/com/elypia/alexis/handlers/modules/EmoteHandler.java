package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Scope;
import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.util.List;

@Module(name = "emote.title", aliases = {"emote", "emoji", "emoticon"}, help = "emote.help")
public class EmoteHandler extends JDAHandler {

    @Command(id = 7, name = "emote.list.title", aliases = "list", help = "emote.list.help")
    @Scope(ChannelType.TEXT)
    public void listEmotes(JDACommand event) {
        listEmotes(event, event.getSource().getGuild());
    }

    @Scope(ChannelType.PRIVATE)
    @Overload(7)
    @Param(name = "guild", help = "emote.list.guild.help")
    public String listEmotes(JDACommand event, Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0)
            return BotUtils.getScript("emote.list.no_emotes", event.getSource());

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        return builder.toString();
    }

    @Command(name = "emote.get.title", aliases = {"get", "post"}, help = "emote.get.help")
    @Param(name = "emote", help = "emote.get.emote")
    public EmbedBuilder post(JDACommand event, Emote emote) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event.getSource().getGuild());
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
