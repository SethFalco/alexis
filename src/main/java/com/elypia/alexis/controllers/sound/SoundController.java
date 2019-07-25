/*
 * Copyright (C) 2019  Elypia
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
