package com.elypia.alexis.controllers.sound;

import com.elypia.alexis.sound.SoundLoader;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public interface SoundController {

    /**
     * Play music from the player if anything in is the playlist.
     * If music is already playing, this does nothing.
     *
     * @return	If this resulting in a change of state.
     */
    boolean play();

    boolean pause();

    SoundLoader add(String query, boolean insert);

    boolean isIdle();

    void removeTrack(String string);

	boolean clearPlaylist();

	void shuffle();

    List<AudioTrack> getQueue();
}
