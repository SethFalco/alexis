package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.steam.SteamUser;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

public class SteamUserBuilder implements IJDABuilder<SteamUser> {

    @Override
    public Message buildEmbed(JDACommand event, SteamUser toSend) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setTitle(toSend.getUsername(), toSend.getProfileUrl());
        builder.setThumbnail(toSend.getAvatarHigh());

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, SteamUser output) {
        return null;
    }
}
