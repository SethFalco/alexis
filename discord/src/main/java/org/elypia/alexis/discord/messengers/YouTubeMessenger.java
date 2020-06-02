/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.messengers;

import com.google.api.services.youtube.model.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.services.youtube.YouTubeService;
import org.elypia.alexis.utils.YouTubeUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.IOException;
import java.text.*;
import java.util.Date;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = SearchResult.class)
public class YouTubeMessenger implements DiscordMessenger<SearchResult> {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeMessenger.class);

    private final YouTubeService youtube;
    private final AlexisMessages messages;

    @Inject
    public YouTubeMessenger(YouTubeService youtube, AlexisMessages messages) {
        this.youtube = youtube;
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, SearchResult output) {
        String videoId = output.getId().getVideoId();
        String url = YouTubeUtils.getVideoUrl(videoId);
        return new MessageBuilder(url).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, SearchResult output) {
        SearchResultSnippet snippet = output.getSnippet();
        String videoId = output.getId().getVideoId();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeUtils.getVideoUrl(videoId));
        builder.setDescription(snippet.getDescription());
        builder.setImage(snippet.getThumbnails().getHigh().getUrl());

        String channelThumbnail = null;

        try {
            channelThumbnail = youtube.getChannelThumbnail(snippet.getChannelId());
            builder.setThumbnail(channelThumbnail);
        } catch (IOException e) {
            logger.error("Obtaining the thumbnail failed, and it isn't cached either.", e);
        }

        try {
            Date date = ISO8601Utils.parse(snippet.getPublishedAt(), new ParsePosition(0));
            builder.setFooter(messages.youtubePublishedOn(date), channelThumbnail);
        } catch (ParseException ex) {
            logger.error("The published at timestamp wasn't formatted as expected.", ex);
        }

        return new MessageBuilder(builder.build()).build();
    }
}
