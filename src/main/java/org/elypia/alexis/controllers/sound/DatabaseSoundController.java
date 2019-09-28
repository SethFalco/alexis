/*
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

package org.elypia.alexis.controllers.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.elypia.alexis.sound.SoundLoader;

import java.util.List;

public class DatabaseSoundController implements SoundController {

    /**
     * Play music from the player if anything in is the playlist.
     * If music is already playing, this does nothing.
     *
     * @return	If this resulting in a change of state.
     */
    public boolean play() {
        if (!player.isPaused())
            return false;

        player.setPaused(false);
        return true;
    }

    public boolean pause() {
        if (player.isPaused())
            return false;

        player.setPaused(true);
        return true;
    }

    public SoundLoader add(String query, boolean insert) {
        SoundLoader loader = new SoundLoader();
        manager.loadItem(query, loader);

        int position = insert || queue.isEmpty() ? 0 : queue.size() - 1;

        List<AudioTrack> tracks = loader.getTracks();

        if (isIdle())
            player.playTrack(tracks.remove(0));

        queue.addAll(position, tracks);
        return loader;
    }

    public boolean isIdle() {
        return player.getPlayingTrack() == null && queue.isEmpty();
    }

    public void removeTrack(String string) {
        queue.remove(string);
    }


	public boolean clearPlaylist() {
		return queue.clear();
	}

	public void shuffle() {
		controller.shuffle();
	}

    public JDA getJDA() {
        return jda;
    }

    public TextChannel getChannel() {
        return jda.getTextChannelById(channel);
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public List<AudioTrack> getQueue() {
        return queue;
    }
}
