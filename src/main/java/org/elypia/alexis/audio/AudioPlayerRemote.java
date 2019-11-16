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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public interface AudioPlayerRemote {

    /**
     * Play music from the player if anything in is the playlist.
     * If music is already playing, this does nothing.
     *
     * @return if this results in a change of state.
     */
    boolean play();

    /**
     * @return if this results in a change of state.
     */
    boolean pause();

//    /**
//     * @param query
//     * @param insert
//     * @return
//     */
//    SoundLoader add(String query, boolean insert);

    /**
     * @return if the audio player is currently active.
     */
    boolean isIdle();

    /**
     * @param string an identifier of the track to remove.
     */
    void removeTrack(String string);

    /**
     * @return if the playlist changes as a result of this call.
     */
	boolean clearPlaylist();

    /**
     * Randomly shuffle all tracks in the queue.
     */
	void shuffle();

    /**
     * @return The current queue of AudioTracks
     */
    List<AudioTrack> getQueue();
}
