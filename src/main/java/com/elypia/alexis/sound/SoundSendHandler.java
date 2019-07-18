package com.elypia.alexis.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class SoundSendHandler implements AudioSendHandler {

	private final AudioPlayer player;
	private AudioFrame lastFrame;

	public SoundSendHandler(final AudioPlayer player) {
		this.player = player;
	}

	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}
