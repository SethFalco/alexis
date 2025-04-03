/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.messengers;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import javax.inject.Inject;

import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.gson.internal.bind.util.ISO8601Utils;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.modules.youtube.YouTubeService;
import fun.falco.alexis.core.modules.youtube.YouTubeUtils;
import fun.falco.alexis.discord.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author seth@falco.fun (Seth Falco)
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
