package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Icon;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.List;

@Icon("fas fa-surprise")
@Module(id = "Emotes", group = "Discord", aliases = {"emote", "emoji", "emoticon"}, help = "emote.help")
public class EmoteModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public EmoteModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

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
            return scripts.get(event.getSource(), "emote.no_emotes");

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
