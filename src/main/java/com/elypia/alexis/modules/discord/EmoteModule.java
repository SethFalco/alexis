package com.elypia.alexis.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.util.List;

@Module(id = "Emote", group = "Discord", aliases = {"emote", "emoji", "emoticon"}, help = "emote.help")
public class EmoteModule extends JDACHandler {

    @Command(id = "Emote", aliases = "list", help = "emote.list.help")
    public void listEmotes(@Channels(ChannelType.TEXT) JDACEvent event) {
        listEmotes(event, event.getSource().getGuild());
    }

    @Overload("Emote")
    @Param(id = "emotes.param.guild.name", help = "emote.list.guild.help")
    public String listEmotes(@Channels(ChannelType.PRIVATE) JDACEvent event, Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0)
            return BotUtils.getScript("emote.no_emotes", event.getSource());

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        return builder.toString();
    }

    @Command(id = "emote.get.title", aliases = {"get", "post"}, help = "emote.get.help")
    @Param(id = "emote", help = "emote.get.emote")
    public EmbedBuilder post(JDACEvent event, Emote emote) {
        EmbedBuilder builder = BotUtils.newEmbed(event.getSource().getGuild());
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
