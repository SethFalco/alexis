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

package org.elypia.alexis.discord.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;

import java.util.*;

public class AudioHandler implements AudioLoadResultHandler {

    private AudioPlaylist playlist;
    private List<AudioTrack> tracks;
    private boolean noResults;
    private FriendlyException ex;

    public AudioHandler() {
        tracks = new ArrayList<>();
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        tracks.add(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        this.playlist = playlist;

        AudioTrack track = playlist.getSelectedTrack();
        tracks.addAll(playlist.getTracks());

        if (track != null)
            tracks.add(0, tracks.remove(tracks.indexOf(track)));
    }

    @Override
    public void noMatches() {
        noResults = true;
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        ex = exception;
    }

    public AudioPlaylist getPlaylist() {
        return playlist;
    }

    public List<AudioTrack> getTracks() {
        return tracks;
    }

    public boolean isResults() {
        return !noResults;
    }

    public FriendlyException getException() {
        return ex;
    }
}
