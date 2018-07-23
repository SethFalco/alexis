package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.elypiai.twitch.TwitchUser;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

public class TwitchUserBuilder implements IJDABuilder<TwitchUser> {

    @Override
    public Message buildEmbed(JDACommand event, TwitchUser output) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

        builder.setAuthor(output.getDisplayName(), output.getUrl());
        builder.setThumbnail(output.getAvatar());
        builder.setDescription(output.getDescription());
        builder.addField("Type", output.getBroadcasterType().toString(), true);
        builder.addField("Total Views", String.format("%,d", output.getViewCount()), true);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, TwitchUser output) {
        return null;
    }
}
