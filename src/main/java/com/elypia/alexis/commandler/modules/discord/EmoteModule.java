package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;

import javax.inject.*;
import java.util.List;

@Singleton
@Module(name = "emotes", group = "Discord", aliases = {"emote", "emoji", "emoticon"})
public class EmoteModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public EmoteModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "list", aliases = "list")
    public String list(CommandlerEvent<Event> event, @Param(name = "guild", defaultValue = "${src.guild.id}") Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

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
    public EmbedBuilder post(CommandlerEvent<Event> event, @Param(name = "emote") Emote emote) {
        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
