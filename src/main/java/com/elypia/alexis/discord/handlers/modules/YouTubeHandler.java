package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.annotations.*;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import com.elypia.elypiai.google.youtube.YouTube;
import com.elypia.elypiai.google.youtube.YouTubeItem;
import net.dv8tion.jda.core.EmbedBuilder;

import static com.elypia.alexis.utils.BotUtils.httpFailure;

@Module(
    aliases = "YouTube",
    help = "Commands related to YouTube."
)
@Developer
public class YouTubeHandler extends CommandHandler {

    private YouTube youtube;

    public YouTubeHandler(String apikey) {
        youtube = new YouTube(apikey);
    }

    @CommandGroup("get")
    @Command(aliases = "get", help = "Search for a video.")
    @Parameter(name = "query", help = "Search term for the video you want.")
    public void getVideo(MessageEvent event, String query) {
        youtube.getVideo(query, result -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(result.getChannelName());
            builder.addField(result.getTitle(), result.getDescription(), true);
            builder.setImage(result.getHighThumbnail());
            builder.setFooter("Published at: " + result.getPublishedDate().toString(), null);

            event.reply(builder);
        }, failure -> httpFailure(event, failure));
    }

    @CommandGroup("get")
    @Parameter(name = "query", help = "Search term for the video you want.")
    @Parameter(name = "count", help = "How many search results to display.")
    public void getVideos(MessageEvent event, String query, int count) {
        EmbedBuilder builder = new EmbedBuilder();

        youtube.getVideos(query, count,  result -> {
            YouTubeItem first = result.get(0);

            builder.setThumbnail(first.getHighThumbnail());
            for (int i = 1; i < result.size(); i++) {
                YouTubeItem item = result.get(i);

            }
        }, failure -> httpFailure(event, failure));
    }
}
