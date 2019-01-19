package com.elypia.alexis.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer player;
	private AudioFrame lastFrame;

	public AudioPlayerSendHandler(final AudioPlayer player) {
		this.player = player;
	}

	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.getData();
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}
