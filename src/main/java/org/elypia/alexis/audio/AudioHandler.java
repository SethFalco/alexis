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

package org.elypia.alexis.audio;

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
