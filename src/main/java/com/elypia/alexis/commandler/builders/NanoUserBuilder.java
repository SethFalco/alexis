package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.nanowrimo.NanoUser;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

public class NanoUserBuilder implements IJDABuilder<NanoUser> {

    @Override
    public Message buildEmbed(JDACommand event, NanoUser output) {
        String name = output.getUsername();
        String wordcount = event.getScript("b.nano.wordcount");

        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setAuthor(name, output.getUrl());

        User user = getUserByNano(event, output);

        if (user != null) {
            String desc = event.getScript("b.nano.description", Map.of(
                "name", name,
                "mention", user.getAsMention()
            ));

            builder.setThumbnail(user.getAvatarUrl());
            builder.setDescription(desc);
        }

        String fieldString = String.format("%,d", output.getWordCount());
        builder.addField(wordcount, fieldString, true);

        if (output.isWinner()) {
            String winner = event.getScript("b.nano.winner", Map.of(
                "name", name
            ));

            builder.setFooter(winner, null);
        }

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, NanoUser output) {
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
    private User getUserByNano(JDACommand event, NanoUser toSend) {
        UserData data = UserData.query("nanowrimo.username", toSend.getUsername());

        if (data != null && !data.getNanoLink().isPrivate()) {
            Message message = event.getMessage();
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
