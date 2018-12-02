package com.elypia.alexis.google.youtube;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.*;
import com.google.api.services.youtube.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class YouTubeHelper {

    private static final List<String> SCOPES = List.of(YouTubeScopes.YOUTUBE_READONLY);

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String THUMBNAIL_FORMAT = "http://img.youtube.com/vi/%s/maxresdefault.jpg";

    private final YouTube youtube;
    private final Map<String, String> channelThumbnailCache;

    public YouTubeHelper(String applicationName) throws IOException, GeneralSecurityException {
        youtube = new YouTube.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            GoogleCredential.getApplicationDefault().createScoped(SCOPES)
        ).setApplicationName(applicationName).build();

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

    public static String toYouTubeTimeFormat(TimeUnit unit, long current, long duration) {
        return String.format("%s / %s", toYouTubeTimeFormat(unit, current), toYouTubeTimeFormat(unit, duration));
    }

    public static String toYouTubeTimeFormat(TimeUnit unit, long time) {
        long seconds = TimeUnit.SECONDS.convert(time, unit);
        StringJoiner joiner = new StringJoiner(":");
        boolean started = false;

        long days = TimeUnit.DAYS.convert(seconds, TimeUnit.SECONDS);
        if (days != 0) {
            seconds -= TimeUnit.SECONDS.convert(days, TimeUnit.DAYS);
            joiner.add(String.valueOf(days));
            started = true;
        }

        long hours = TimeUnit.HOURS.convert(seconds, TimeUnit.SECONDS);
        if (hours != 0 || started) {
            seconds -= TimeUnit.SECONDS.convert(hours, TimeUnit.HOURS);

            if (started)
                joiner.add(String.format("%02d", hours));
            else
                joiner.add(String.valueOf(hours));

            started = true;
        }

        long minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

        if (started)
            joiner.add(String.format("%02d", minutes));
        else
            joiner.add(String.valueOf(minutes));

        joiner.add(String.format("%02d", seconds));
        return joiner.toString();
    }

    /**
     * Forms the YouTube video url from an identifier provided. <br>
     * Do note: Does not use an API call.
     * Simple prepends standard youtube video url.
     *
     * @param id The identifier of the video.
     * @return Url to the video id provided.
     */

    public static String getVideoUrl(String id) {
        return VIDEO_URL + id;
    }

    /**
     * Forms the thumbnail url from an identifier provided. <br>
     * Do note: Does not use an API call.
     * Simply inserts id into standard thumbnail url.
     *
     * @param id The identifier of the video.
     * @return Url to the high quality youtube thumbnail.
     */

    public static String getThumbnailUrl(String id) {
        return String.format(THUMBNAIL_FORMAT, id);
    }
}
