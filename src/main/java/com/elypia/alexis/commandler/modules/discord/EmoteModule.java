package com.elypia.alexis.commandler.modules.discord;

import com.elypia.alexis.commandler.dyndefault.CurrentGuild;
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
@Module(name = "Emotes", group = "Discord", aliases = {"emote", "emoji", "emoticon"}, help = "Interact with Discord emotes!")
public class EmoteModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public EmoteModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "List Emotes", aliases = "list", help = "List all emotes in a guild.")
    public String list(
        CommandlerEvent<Event> event,
        @Param(name = "guild", help = "The guild to list emotes from.", dynDefaultValue = CurrentGuild.class) Guild guild) {
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

    @Command(name = "Send Emote", aliases = {"get", "post"}, help = "Send an emote in chat.")
    public EmbedBuilder post(
        CommandlerEvent<Event> event,
        @Param(name = "emote", help = "The emote to send.") Emote emote
    ) {
        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
