package com.elypia.alexis.discord.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class AudioDispatcher extends AudioEventAdapter {

	private final AudioController controller;

	public AudioDispatcher(final AudioController controller) {
		this.controller = controller;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		controller.getChannel().sendMessage("Played a song").queue();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {

	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException ex) {

	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long threshold) {

	}
}
