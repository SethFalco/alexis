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

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class GuildPlayer {

	private JDA jda;
	private long channel;

	private AudioPlayerManager manager;
	private AudioPlayer player;

	private List<AudioTrack> queue;

	public GuildPlayer(JDA jda, AudioPlayerManager manager) {
		this.jda = jda;
		this.manager = manager;

		player = manager.createPlayer();
		player.addListener(new AudioPlayerDispatcher(this));
		queue = new ArrayList<>();
	}

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

	public AudioHandler add(String query, boolean insert) {
		AudioHandler loader = new AudioHandler();
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

//	public boolean clearPlaylist() {
//		return queue.clear();
//	}
//
//	public void shuffle() {
//		controller.shuffle();
//	}

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
