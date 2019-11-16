/*
 * Alexis - A general purpose chatbot for Discord.
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

package org.elypia.alexis.services.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.*;
import com.google.api.services.youtube.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.elypia.commandler.config.AppConfig;

import javax.inject.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class YouTubeService {

    /** The OAuth scopes required by this application. */
    private static final List<String> SCOPES = List.of(YouTubeScopes.YOUTUBE_READONLY);

    /** The YouTube instance which wraps around the YouTube API. */
    private final YouTube youtube;

    /** A runtime cache that stores channel thumbnails against video/playlist urls. */
    private final Map<String, String> channelThumbnailCache;

    @Inject
    public YouTubeService(AppConfig appConfig) throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped(SCOPES);

        youtube = new YouTube.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            new HttpCredentialsAdapter(credentials)
        ).setApplicationName(appConfig.getApplicationName()).build();

        channelThumbnailCache = new HashMap<>();
    }

    public Optional<SearchResult> getSearchResult(String query, ResourceType type) throws IOException {
        List<SearchResult> results = getSearchResults(query, type, 1);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<SearchResult> getSearchResults(String query, ResourceType type, long limit) throws IOException {
        YouTube.Search.List search = youtube.search().list("snippet");
        search.setQ(query);
        search.setMaxResults(limit);
        search.setType(type.getName());

        SearchListResponse response = search.execute();
        return response.getItems();
    }

    public String getChannelThumbnail(String id) throws IOException {
        return getChannelThumbnail(id, false);
    }

    public String getChannelThumbnail(final String id, boolean video) throws IOException {
        if (channelThumbnailCache.containsKey(id))
            return channelThumbnailCache.get(id);

        String channelId = id;

        if (video) {
            Optional<SearchResult> result = getSearchResult(id, ResourceType.VIDEO);

            if (result.isPresent())
                channelId = result.get().getSnippet().getChannelId();
            else
                return null;
        }

        Optional<SearchResult> result = getSearchResult(channelId, ResourceType.CHANNEL);

        if (result.isPresent()) {
            String url = result.get().getSnippet().getThumbnails().getHigh().getUrl();
            channelThumbnailCache.put(id, url);
            return url;
        }

        return null;
    }
}
