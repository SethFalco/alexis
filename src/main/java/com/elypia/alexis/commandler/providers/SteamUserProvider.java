package com.elypia.alexis.commandler.providers;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.discord.interfaces.DiscordProvider;
import com.elypia.elypiai.steam.SteamUser;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

@Provider(provides = Message.class, value = SteamUser.class)
public class SteamUserProvider implements DiscordProvider<SteamUser> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, SteamUser output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, SteamUser toSend) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setTitle(toSend.getUsername(), toSend.getProfileUrl());
        builder.setThumbnail(toSend.getAvatarHigh());

        return new MessageBuilder(builder.build()).build();
    }
}
