package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.youtube.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.modules.CommandHandler;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.IOException;
import java.util.Optional;

@Module(name = "YouTube", aliases = {"youtube", "yt"}, description = "Commands related to YouTube.")
public class YouTubeHandler extends CommandHandler {

    private YouTubeHelper youtube;

    public YouTubeHandler(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Command(name = "Search YouTube", aliases = "get", help = "Search for a video.")
    @Param(name = "query", help = "Search term for the video you want.")
    public Object getVideo(String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);

        if (!searchResult.isPresent())
            return "Sorry, I couldn't find any results for that on YouTube!";

        SearchResult result = searchResult.get();
        SearchResultSnippet snippet = result.getSnippet();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeHelper.getVideoUrl(result.getId().getVideoId()));
        builder.setThumbnail(youtube.getChannelThumbnail(snippet.getChannelId()));
        builder.setDescription(snippet.getDescription());
        builder.setImage(snippet.getThumbnails().getHigh().getUrl());
        builder.setFooter("Published at: " + snippet.getPublishedAt().toString(), null);

        return builder;
    }
}
