package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.elypiai.twitch.User;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

@Compatible(User.class)
public class TwitchUserBuilder implements IJDACBuilder<User> {

    @Override
    public Message buildEmbed(JDACEvent event, User output) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setAuthor(output.getDisplayName(), output.getUrl());
        builder.setThumbnail(output.getAvatar());
        builder.setDescription(output.getDescription());
        builder.addField("Type", output.getBroadcasterType().toString(), true);
        builder.addField("Total Views", String.format("%,d", output.getViewCount()), true);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, User output) {
        return null;
    }
}
