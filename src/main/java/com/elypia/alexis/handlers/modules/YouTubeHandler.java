package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.google.youtube.*;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.elyscript.ElyScript;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.Optional;

@Module(name = "YouTube", aliases = {"youtube", "yt"}, help = "Commands related to YouTube.")
public class YouTubeHandler extends JDAHandler {

    private static final ElyScript VIDEO_NO_RESULTS = new ElyScript("(Sorry|I apologise), I (couldn't|could not) (find|get|obtain) (any ){?}(videos|(search ){?}results) for that( on YouTube){?}!");

    private YouTubeHelper youtube;

    public YouTubeHandler(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Command(id = 10, name = "Search YouTube", aliases = "get", help = "Search for a video.")
    @Param(name = "query", help = "Search term for the video you want.")
    @Emoji(emotes = "\uD83C\uDFB5", help = "Add this video to the current queue if you're listening to music.")
    public Object getVideo(JDACommand event, String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);

        if (!searchResult.isPresent())
            return VIDEO_NO_RESULTS.compile();

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
