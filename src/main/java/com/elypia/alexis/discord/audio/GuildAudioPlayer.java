package com.elypia.alexis.discord.audio;

import com.elypia.alexis.discord.audio.controllers.impl.AudioController;
import com.elypia.commandler.events.MessageEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class GuildAudioPlayer {

	private JDA jda;

	private AudioPlayerManager manager;
	private AudioPlayer player;
	private AudioController controller;

	public GuildAudioPlayer(Class<? extends AudioController> clazz, AudioPlayerManager manager, MessageEvent event) {
		this.manager = manager;
		this.controller = AudioController.getInstance(clazz, this);

		player = manager.createPlayer();

		jda = event.getJDA();

		player.addListener(new AudioDispatcher(controller));
	}

	/**
	 * Play music from the player if anything in is the playlist.
	 * If music is already playing, this does nothing.
	 *
	 * @return	If it was already playing.
	 */

	public boolean play() {
		boolean paused = player.isPaused();

		if (!paused)
			return true;

		player.setPaused(false);
		return false;
	}

	public boolean pause() {
		boolean paused = player.isPaused();

		if (paused)
			return true;

		player.setPaused(true);
		return false;
	}

	public void addTrack(String query) {
		addTrack(query, false);
	}

	public void insertTrack(String query) {
		addTrack(query, true);
	}

	private void addTrack(String query, boolean start) {
		manager.loadItem(query, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				if (controller.getSize() == 0 && player.getPlayingTrack() == null)
					player.playTrack(track);
				else
					controller.add(track, start ? 0 : Integer.MAX_VALUE);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				playlist.getTracks().forEach(o -> {
					controller.add(o, start ? 0 : Integer.MAX_VALUE);
				});
			}

			@Override
			public void noMatches() {

			}

			@Override
			public void loadFailed(FriendlyException exception) {

			}
		});
	}

	public void removeTrack(String string) {
		controller.remove(string);
	}

	public boolean clearPlaylist() {
		return controller.clear();
	}

	public void shuffle() {
		controller.shuffle();
	}

	public JDA getJDA() {
		return jda;
	}

	public TextChannel getChannel() {
		return controller.getChannel();
	}

	public void setChannel(TextChannel channel) {
		controller.setChannel(channel);
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public AudioTrack getPlayingTrack() {
		return player.getPlayingTrack();
	}

	public List<AudioTrack> getTracks() {
		return controller.getTracks();
	}
}
