package com.elypia.alexis.providers;

import com.elypia.alexis.services.youtube.YouTubeService;
import com.elypia.alexis.utils.*;
import com.elypia.cmdlrdiscord.interfaces.DiscordProvider;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

import javax.inject.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
@Provider(provides = Message.class, value = SearchResult.class)
public class YouTubeProvider implements DiscordProvider<SearchResult> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd MMM YYYY");

    private YouTubeService youtube;

    @Inject
    public YouTubeProvider(YouTubeService youtube) {
        this.youtube = youtube;
    }

    @Override
    public Message buildMessage(CommandlerEvent<?> event, SearchResult output) {
        String videoId = output.getId().getVideoId();
        String url = YouTubeUtils.getVideoUrl(videoId);
        return new MessageBuilder(url).build();
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, SearchResult output) {
        SearchResultSnippet snippet = output.getSnippet();
        String videoId = output.getId().getVideoId();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeUtils.getVideoUrl(videoId));
        builder.setDescription(snippet.getDescription());
        builder.setImage(YouTubeUtils.getThumbnailUrl(videoId));

        DateTime datetime = snippet.getPublishedAt();
        long milli = datetime.getValue();
        Date date = new Date(milli);

        builder.setFooter("Published on " + FORMAT.format(date), null);

        try {
            builder.setThumbnail(youtube.getChannelThumbnail(snippet.getChannelId()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MessageBuilder(builder.build()).build();
    }
}
