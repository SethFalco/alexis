package com.elypia.alexis.audio;

import com.elypia.elypiai.utils.Tuple;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class GuildAudioPlayer {

	private JDA jda;
	private long channel;

	private AudioPlayerManager manager;
	private AudioPlayer player;
	private List<AudioTrack> queue;

	public GuildAudioPlayer(JDA jda, AudioPlayerManager manager) {
		this.jda = jda;
		this.manager = manager;

		player = manager.createPlayer();
		player.addListener(new AudioDispatcher(this));
		queue = new ArrayList<>();
	}

	/**
	 * Play music from the player if anything in is the playlist.
	 * If music is already playing, this does nothing.
	 *
	 * @return	If this resulting in a change of state.
	 */
	public boolean play() {
		boolean paused = player.isPaused();

		if (!paused)
			return false;

		player.setPaused(false);
		return true;
	}

	public boolean pause() {
		boolean paused = player.isPaused();

		if (paused)
			return false;

		player.setPaused(true);
		return true;
	}

	public Tuple<AudioPlaylist, List<AudioTrack>> add(String query, boolean insert) {
		Tuple<AudioPlaylist, List<AudioTrack>> tuple = getTracks(query);

		if (tuple == null)
			return null;

		AudioPlaylist playlist = tuple.itemOne();
		List<AudioTrack> tracks = tuple.itemTwo();
		AudioTrack selectedTrack = playlist != null ? playlist.getSelectedTrack() : null;
		int position = insert || queue.isEmpty() ? 0 : queue.size() - 1;

		if (selectedTrack != null) {
			if (isIdle())
				player.playTrack(selectedTrack);
			else
				queue.add(position, selectedTrack);

			return tuple;
		}

		if (isIdle()) {
			player.playTrack(tracks.get(0));

			if (tracks.size() > 1)
				queue.addAll(position, tracks.subList(1, tracks.size()));
		}

		else
			queue.addAll(position, tracks);

		return tuple;
	}

	private Tuple<AudioPlaylist, List<AudioTrack>> getTracks(String query) {
		Tuple<AudioPlaylist, List<AudioTrack>> tuple = Tuple.of(null, null);

		try {
			manager.loadItem(query, new AudioLoadResultHandler() {

				@Override
				public void trackLoaded(AudioTrack track) {
					ArrayList<AudioTrack> tracks = new ArrayList<>();
					tracks.add(track);
					tuple.itemTwo(tracks);
				}

				@Override
				public void playlistLoaded(AudioPlaylist playlist) {
					tuple.itemOne(playlist);
					tuple.itemTwo(playlist.getTracks());
				}

				@Override
				public void noMatches() {

				}

				@Override
				public void loadFailed(FriendlyException exception) {

				}
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return (tuple.itemOne() == null && tuple.itemTwo() == null) ? null : tuple;
	}

	public boolean isIdle() {
		return player.getPlayingTrack() == null && queue.isEmpty();
	}
//
//	public void removeTrack(String string) {
//		controller.remove(string);
//	}
//
//	public boolean clearPlaylist() {
//		return controller.clear();
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
