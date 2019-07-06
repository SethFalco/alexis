package com.elypia.alexis.commandler.providers;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.discord.interfaces.DiscordProvider;
import com.elypia.elypiai.twitch.User;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

@Provider(provides = Message.class, value = User.class)
public class StreamerProvider implements DiscordProvider<User> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, User output) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setAuthor(output.getDisplayName(), output.getUrl());
        builder.setThumbnail(output.getAvatar());
        builder.setDescription(output.getDescription());
        builder.addField("Type", output.getBroadcasterType().toString(), true);
        builder.addField("Total Views", String.format("%,d", output.getViewCount()), true);

        return new MessageBuilder(builder.build()).build();
    }
}
