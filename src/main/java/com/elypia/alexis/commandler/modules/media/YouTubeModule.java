package com.elypia.alexis.commandler.modules.media;

import com.elypia.alexis.services.youtube.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.jdac.alias.*;
import com.google.api.services.youtube.model.SearchResult;
import com.google.inject.*;

import java.io.IOException;
import java.util.Optional;

@Singleton
@Module(name = "YouTube", group = "Media", aliases = {"youtube", "yt"}, help = "yt.h")
public class YouTubeModule implements Handler {

    private YouTubeService youtube;

    @Inject
    public YouTubeModule(YouTubeService youtube) {
        this.youtube = youtube;
    }

    @Command(name = "Search", aliases = "get", help = "yt.search.h")
    @Param(name = "common.query", help = "yt.search.p.query.h")
//    @Emoji(emotes = "\uD83C\uDFA7", help = "yt.search.e.headphones")
    public Object getVideo(JDACEvent event, String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);

        if (!searchResult.isPresent())
            return event.send("yt.search.no_results");

        SearchResult result = searchResult.get();
//        event.addReaction("\uD83C\uDFB5");
//        event.storeObject("url", YouTubeService.getVideoUrl(result.getId().getVideoId()));
        return searchResult.get();
    }

    @Reaction(command = "search", emotes = "\uD83C\uDFB5")
    public void addToQueue(ReactionEvent event) {
        event.trigger("music add " + event.getReactionRecord().getObject("url"));
        event.deleteParentMessage();
        event.deleteMessage();
    }
}
