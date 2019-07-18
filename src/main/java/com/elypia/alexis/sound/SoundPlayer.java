package com.elypia.alexis.sound;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class SoundPlayer {

	private JDA jda;
	private long channel;

	private AudioPlayerManager manager;
	private AudioPlayer player;

	private List<AudioTrack> queue;

	public SoundPlayer(JDA jda, AudioPlayerManager manager) {
		this.jda = jda;
		this.manager = manager;

		player = manager.createPlayer();
		player.addListener(new SoundDispatcher(this));
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
