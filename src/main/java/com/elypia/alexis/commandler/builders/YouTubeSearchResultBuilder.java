package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public class YouTubeSearchResultBuilder implements IJDABuilder<SearchResult> {

    private YouTubeHelper youtube;

    public YouTubeSearchResultBuilder(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Override
    public Message buildEmbed(JDACommand event, SearchResult output) {
        SearchResultSnippet snippet = output.getSnippet();

        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeHelper.getVideoUrl(output.getId().getVideoId()));
        builder.setDescription(snippet.getDescription());
        builder.setImage(snippet.getThumbnails().getHigh().getUrl());
        builder.setFooter("Published at: " + snippet.getPublishedAt().toString(), null);

        try {
            builder.setThumbnail(youtube.getChannelThumbnail(snippet.getChannelId()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, SearchResult output) {
        return null;
    }
}
