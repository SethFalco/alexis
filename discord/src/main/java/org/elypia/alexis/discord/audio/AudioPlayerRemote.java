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
