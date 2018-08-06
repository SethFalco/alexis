package com.elypia.alexis.audio;

import com.elypia.alexis.entities.GuildData;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.core.entities.*;

public class AudioDispatcher extends AudioEventAdapter {

	private GuildAudioPlayer guildAudioPlayer;

	public AudioDispatcher(GuildAudioPlayer guildAudioPlayer) {
		this.guildAudioPlayer = guildAudioPlayer;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		TextChannel channel = guildAudioPlayer.getChannel();
		Guild guild = channel.getGuild();
		AudioTrackInfo info = track.getInfo();
		GuildData data = GuildData.query(guild.getIdLong());

//		EmbedBuilder builder = new EmbedBuilder();
//		builder.setTitle("Now Playing");
//		builder.setDescription(Markdown.a(info.author + " - " + info.title, info.uri));
//
//		if (track.getSourceManager() instanceof YoutubeAudioSourceManager) {
//			builder.setThumbnail("");
//			builder.setImage(YouTube.getVideoUrl(info.identifier));
//		}

		if (data.getSettings().getMusicSettings().getSyncChannelName()) {
			Member member = guild.getSelfMember();
			String name = member.getUser().getName() + " \uD83C\uDFB5 ";
			String title = info.title + " - " + info.author;
			name += title;

			if (name.length() > 32)
				name = name.substring(0, 30) + "...";

			guild.getController().setNickname(member, name).queue();
		}
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
