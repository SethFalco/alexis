package com.elypia.alexis.providers;

import com.elypia.alexis.utils.DiscordUtils;
import com.elypia.cmdlrdiscord.interfaces.DiscordProvider;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.interfaces.LanguageInterface;
import com.elypia.elypiai.nanowrimo.Writer;
import com.google.inject.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;

@Singleton
@Provider(provides = Message.class, value = Writer.class)
public class WriterProvider implements DiscordProvider<Writer> {

    private final LanguageInterface language;

    @Inject
    public WriterProvider(LanguageInterface language) {
        this.language = language;
    }

    @Override
    public Message buildMessage(CommandlerEvent<?> event, Writer output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, Writer output) {
        String name = output.getUsername();
        String wordcount = language.get("b.nano.wordcount");

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(name, output.getUrl());

        String fieldString = String.format("%,d", output.getWordCount());
        builder.addField(wordcount, fieldString, true);

        if (output.isWinner()) {
            String winner = language.get("b.nano.winner", Map.of(
                "name", name
            ));

            builder.setFooter(winner, null);
        }

        return new MessageBuilder(builder.build()).build();
    }
}
