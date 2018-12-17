package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.IScripts;
import com.elypia.elypiai.nanowrimo.Writer;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.*;

@Compatible(Writer.class)
public class WriterBuilder implements IJDACBuilder<Writer> {

    @Override
    public Message buildEmbed(JDACEvent event, Writer output) {
        IScripts scripts = event.getScripts();

        String name = output.getUsername();
        String wordcount = scripts.get("b.nano.wordcount");

        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setAuthor(name, output.getUrl());

        User user = getUserByNano(event, output);

        if (user != null) {
            String desc = scripts.get("b.nano.description", Map.of(
                "name", name,
                "mention", user.getAsMention()
            ));

            builder.setThumbnail(user.getAvatarUrl());
            builder.setDescription(desc);
        }

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

    /**
     * This returns a user that owns this NanoWriMo account if they meet all conditions.
     * The owner of the NaNoWriMo account has authenticated. <br>
     * The link is not set to private. <br>
     * If performed in a guild, the NaNoWriMo user is in the same guild. <br>
     * If performed in PM, the NaNoWriMo user is in a mutual guild. <br>
     * <br>
     * If at least one of these are voided, null is returned.
     *
     * @param event The event that trigged this command.
     * @param toSend The NaNoWriMo user we're sending in chat.
     * @return The user that owns this account if conditions are met, else null.
     */
    private User getUserByNano(JDACEvent event, Writer toSend) {
        UserData data = UserData.query("nanowrimo.username", toSend.getUsername());

        if (data != null && !data.getNanoLink().isPrivate()) {
            MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();
            Message message = source.getMessage();
            JDA jda = message.getJDA();
            User user = jda.getUserById(data.getUserId());

            if (user != null) {
                boolean isGuild = message.getChannelType().isGuild();

                if (isGuild) {
                    if (message.getGuild().isMember(user))
                        return user;
                } else {
                    List<Guild> guilds = message.getAuthor().getMutualGuilds();

                    if (guilds.stream().anyMatch(g -> g.isMember(user)))
                        return user;
                }
            }
        }

        return null;
    }
}
