package com.elypia.alexis.handlers.modules.media;

import com.elypia.alexis.google.youtube.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.Optional;

@Module(name = "yt", group = "Media", aliases = {"youtube", "yt"}, help = "yt.h")
public class YouTubeHandler extends JDAHandler {

    private YouTubeHelper youtube;

    public YouTubeHandler(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Command(id = 10, name = "yt.search", aliases = "get", help = "yt.search.h")
    @Param(name = "common.query", help = "yt.search.p.query.h")
    @Emoji(emotes = "\uD83C\uDFA7", help = "yt.search.e.headphones")
    public Object getVideo(JDACommand event, String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);

        if (!searchResult.isPresent())
            return event.getScript("yt.search.no_results");

        SearchResult result = searchResult.get();
//        event.addReaction("\uD83C\uDFB5");
//        event.storeObject("url", YouTubeHelper.getVideoUrl(result.getId().getVideoId()));
        return searchResult.get();
    }

//    @Reaction(id = 10, emotes = "\uD83C\uDFB5")
//    public void addToQueue(ReactionEvent event) {
//        event.trigger("music add " + event.getReactionRecord().getObject("url"));
//        event.deleteParentMessage();
//        event.deleteMessage();
//    }
}
