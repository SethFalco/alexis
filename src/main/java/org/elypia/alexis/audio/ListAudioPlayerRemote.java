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
public class ListAudioPlayerRemote implements AudioPlayerRemote {

    @Override
    public boolean play() {
        return false;
    }

    @Override
    public boolean pause() {
        return false;
    }

//    @Override
//    public SoundLoader add(String query, boolean insert) {
//        return null;
//    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public void removeTrack(String string) {

    }

    @Override
    public boolean clearPlaylist() {
        return false;
    }

    @Override
    public void shuffle() {

    }

    @Override
    public List<AudioTrack> getQueue() {
        return null;
    }
}
