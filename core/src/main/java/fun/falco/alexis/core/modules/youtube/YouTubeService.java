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

package fun.falco.alexis.core.modules.youtube;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import fun.falco.alexis.core.configuration.AppConfig;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
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

    public SubscriptionListResponse getChannelSubscriptions(String channelId) throws IOException {
        return getChannelSubscriptions(channelId, 50);
    }

    /**
     * @param channelId Channel to get subscriptions from.
     * @param limit Maximum number of subscriptions to get.
     * @return List of subscriptions from the API.
     * @throws IOException
     */
    public SubscriptionListResponse getChannelSubscriptions(String channelId, long limit) throws IOException {
        var request = youtube.subscriptions().list(List.of("subscriberSnippet", "snippet"));
        request.setChannelId(channelId);
        request.setMaxResults(limit);

        return request.execute();
    }

    public Optional<SearchResult> getSearchResult(String query, ResourceType type) throws IOException {
        List<SearchResult> results = getSearchResults(query, type, 1);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<SearchResult> getSearchResults(String query, ResourceType type, long limit) throws IOException {
        YouTube.Search.List search = youtube.search().list(List.of("snippet"));
        search.setQ(query);
        search.setMaxResults(limit);
        search.setType(List.of(type.getName()));

        SearchListResponse response = search.execute();
        return response.getItems();
    }

    public String getChannelThumbnail(String channelId) throws IOException {
        if (channelThumbnailCache.containsKey(channelId)) {
            return channelThumbnailCache.get(channelId);
        }

        var list = youtube.channels().list(List.of("snippet"));
        list.setMaxResults(1L);
        list.setId(List.of(channelId));

        ChannelListResponse result = list.execute();
        String url = result.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl();

        channelThumbnailCache.put(channelId, url);
        return url;
    }

    public String getChannelThumbnailFromVideoId(String videoId) throws IOException {
        Optional<SearchResult> result = getSearchResult(videoId, ResourceType.VIDEO);

        if (result.isEmpty()) {
            return null;
        }

        String channelId = result.get().getSnippet().getChannelId();
        return getChannelThumbnail(channelId);
    }
}
