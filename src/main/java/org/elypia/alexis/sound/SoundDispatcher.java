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

package org.elypia.alexis.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.ChatBot;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.services.youtube.YouTubeService;

public class SoundDispatcher extends AudioEventAdapter {

	private SoundPlayer soundPlayer;

	public SoundDispatcher(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		TextChannel channel = soundPlayer.getChannel();
		Guild guild = channel.getGuild();
		AudioTrackInfo info = track.getInfo();
		GuildData data = ChatBot.getDatabaseManager().query(GuildData.class, "guild_id", guild.getIdLong());

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Now Playing");
		builder.setDescription(Md.a(info.author + " - " + info.title, info.uri));

		if (track.getSourceManager() instanceof YoutubeAudioSourceManager) {
			builder.setThumbnail("");
			builder.setImage(YouTubeService.getVideoUrl(info.identifier));
		}

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
