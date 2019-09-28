/*
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.providers;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.services.youtube.YouTubeService;
import org.elypia.alexis.utils.*;
import org.elypia.comcord.interfaces.DiscordProvider;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.Provider;

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
