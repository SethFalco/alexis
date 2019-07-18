package com.elypia.alexis.modules.discord;

import com.elypia.alexis.utils.DiscordUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;

import javax.inject.*;
import java.util.*;

@Singleton
@Module(name = "emote", group = "discord", aliases = {"emote", "emoji", "emoticon"})
public class EmoteModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public EmoteModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "emote.list", aliases = "emote.list.h")
    public String list(CommandlerEvent<Event, Message> event, @Param(name = "p.guild", defaultValue = "${source.guild}") Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        PropertyResourceBundle.getBundle("AlexisMessages", Locale.ENGLISH);

        if (count == 0)
            return lang.get(event, "emote.no_emotes");

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        return builder.toString();
    }

    @Command(name = "send", aliases = {"get", "post"})
    public EmbedBuilder post(CommandlerEvent<Event, Message> event, @Param(name = "emote") Emote emote) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
