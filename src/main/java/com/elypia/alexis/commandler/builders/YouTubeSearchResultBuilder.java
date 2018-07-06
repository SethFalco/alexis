package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.youtube.YouTubeHelper;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageBuilder;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.io.IOException;

public class YouTubeSearchResultBuilder implements IMessageBuilder<SearchResult> {

    private YouTubeHelper youtube;

    public YouTubeSearchResultBuilder(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, SearchResult toSend) {
        SearchResultSnippet snippet = toSend.getSnippet();

        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeHelper.getVideoUrl(toSend.getId().getVideoId()));
        builder.setDescription(snippet.getDescription());
        builder.setImage(snippet.getThumbnails().getHigh().getUrl());
        builder.setFooter("Published at: " + snippet.getPublishedAt().toString(), null);

        try {
            builder.setThumbnail(youtube.getChannelThumbnail(snippet.getChannelId()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, SearchResult... toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, SearchResult toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, SearchResult... toSend) {
        return null;
    }
}
