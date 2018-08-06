package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.nanowrimo.NanoUser;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.List;

public class NanoUserBuilder implements IJDABuilder<NanoUser> {

    @Override
    public Message buildEmbed(JDACommand event, NanoUser output) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);
        User user = getUserByNano(event, output);

        builder.setAuthor(output.getUsername(), output.getUrl());

        if (user != null) {
            builder.setThumbnail(user.getAvatarUrl());
            builder.setDescription("They are " + user.getAsMention() + " on Discord!");
        }

        String fieldString = String.format("%,d", output.getWordCount());
        builder.addField("Total Word Count", fieldString, true);

        if (output.isWinner()) {
            String footerFormat = "%s is a winner of the last NaNoWriMo event!";
            builder.setFooter(String.format(footerFormat, output.getUsername()), null);
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
