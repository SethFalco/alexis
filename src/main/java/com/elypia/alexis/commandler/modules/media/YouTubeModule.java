package com.elypia.alexis.commandler.modules.media;

import com.elypia.alexis.google.youtube.*;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.jdac.alias.*;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.io.IOException;
import java.util.Optional;

@Module(id = "YouTube", group = "Media", aliases = {"youtube", "yt"}, help = "yt.h")
public class YouTubeModule extends JDACHandler {

    private YouTubeHelper youtube;

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public YouTubeModule(Commandler<GenericMessageEvent, Message> commandler, YouTubeHelper youtube) {
        super(commandler);
        this.youtube = youtube;
    }

    @Command(id = "Search", aliases = "get", help = "yt.search.h")
    @Param(id = "common.query", help = "yt.search.p.query.h")
//    @Emoji(emotes = "\uD83C\uDFA7", help = "yt.search.e.headphones")
    public Object getVideo(JDACEvent event, String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);

        if (!searchResult.isPresent())
            return event.send("yt.search.no_results");

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
