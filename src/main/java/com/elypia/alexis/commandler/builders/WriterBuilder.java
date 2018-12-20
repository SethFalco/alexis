package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.IScripts;
import com.elypia.elypiai.nanowrimo.Writer;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.Map;

@Compatible(Writer.class)
public class WriterBuilder implements IJDACBuilder<Writer> {

    @Override
    public Message buildEmbed(JDACEvent event, Writer output) {
        IScripts scripts = event.getScripts();

        String name = output.getUsername();
        String wordcount = scripts.get("b.nano.wordcount");

        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setAuthor(name, output.getUrl());

        String fieldString = String.format("%,d", output.getWordCount());
        builder.addField(wordcount, fieldString, true);

        if (output.isWinner()) {
            String winner = scripts.get("b.nano.winner", Map.of(
                "name", name
            ));

            builder.setFooter(winner, null);
        }

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, Writer output) {
        IScripts scripts = event.getScripts();

        return null;
    }
}
