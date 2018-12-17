package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.elypiai.steam.SteamUser;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

@Compatible(SteamUser.class)
public class SteamUserBuilder implements IJDACBuilder<SteamUser> {

    @Override
    public Message buildEmbed(JDACEvent event, SteamUser toSend) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setTitle(toSend.getUsername(), toSend.getProfileUrl());
        builder.setThumbnail(toSend.getAvatarHigh());

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, SteamUser output) {
        return null;
    }
}
