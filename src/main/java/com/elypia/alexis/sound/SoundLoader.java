package com.elypia.alexis.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;

import java.util.*;

public class SoundLoader implements AudioLoadResultHandler {

    private AudioPlaylist playlist;
    private List<AudioTrack> tracks;
    private boolean noResults;
    private FriendlyException ex;

    public SoundLoader() {
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
