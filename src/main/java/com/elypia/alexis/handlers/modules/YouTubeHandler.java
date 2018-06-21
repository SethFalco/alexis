package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.google.youtube.*;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(name = "YouTube", aliases = {"youtube", "yt"}, description = "Commands related to YouTube.")
public class YouTubeHandler extends CommandHandler {

    private YouTube youtube;

    public YouTubeHandler(String apikey) {
        youtube = new YouTube(apikey);
    }

    @Overload("get")
    @Command(name = "Search YouTube", aliases = "get", help = "Search for a video.")
    @Param(name = "query", help = "Search term for the video you want.")
    public void getVideo(MessageEvent event, String query) {
        youtube.getVideo(query, result -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(result.getChannelName());
            builder.setTitle(result.getTitle(), result.getUrl());
            builder.setDescription(result.getDescription());
            builder.setImage(result.getHighThumbnail());
            builder.setFooter("Published at: " + result.getPublishedDate().toString(), null);

            event.reply(builder);
        }, failure -> BotUtils.sendHttpError(event, failure));
    }

    @Overload("get")
    @Param(name = "query", help = "Search term for the video you want.")
    @Param(name = "count", help = "How many search results to display.")
    public void getVideos(MessageEvent event, String query, int count) {
        EmbedBuilder builder = new EmbedBuilder();

        youtube.getVideos(query, count,  result -> {
            YouTubeItem first = result.get(0);

            builder.setThumbnail(first.getHighThumbnail());
            for (int i = 1; i < result.size(); i++) {
                YouTubeItem item = result.get(i);

            }
        }, failure -> BotUtils.sendHttpError(event, failure));
    }
}
