package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.google.youtube.YouTubeHelper;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YouTubeSearchResultBuilder implements IJDABuilder<SearchResult> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd MMM YYYY");

    private YouTubeHelper youtube;

    public YouTubeSearchResultBuilder(YouTubeHelper youtube) {
        this.youtube = youtube;
    }

    @Override
    public Message buildEmbed(JDACommand event, SearchResult output) {
        SearchResultSnippet snippet = output.getSnippet();
        String videoId = output.getId().getVideoId();

        EmbedBuilder builder = BotUtils.newEmbed(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeHelper.getVideoUrl(videoId));
        builder.setDescription(snippet.getDescription());
        builder.setImage(YouTubeHelper.getThumbnailUrl(videoId));

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

    @Override
    public Message build(JDACommand event, SearchResult output) {
        String videoId = output.getId().getVideoId();
        String url = YouTubeHelper.getVideoUrl(videoId);
        return new MessageBuilder(url).build();
    }
}
