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

package org.elypia.alexis.discord.controllers;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.elypia.alexis.discord.audio.GuildPlayer;
import org.elypia.alexis.services.youtube.YouTubeService;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.slf4j.*;

import javax.inject.Inject;
import java.util.*;

@Exclude
@CommandController
@StandardCommand
public class MusicController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

	private static final int LINE_MAX_LENGTH = 55;

	private static final String[] NUMBERS = {
		"1⃣", "2⃣","3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣", "9⃣"
	};

	/**
	 * This allows us to query YouTube for information on videos,
	 * channels or playlists.
	 */
	private final YouTubeService youtube;

	/**
	 * The manager class which allows us to create new sound players
	 * (for each guild) and parse tracks as {@link AudioTrack}s.
	 */
	private final DefaultAudioPlayerManager manager;

	/**
	 * A map of Guild IDs to sound players, as each guild owns their own
	 * independent music player seperate from others.
	 */
	private final Map<Long, GuildPlayer> guildPlayers;

	@Inject
	public MusicController(final YouTubeService youtube) {
		this.youtube = youtube;
		manager = new DefaultAudioPlayerManager();
		guildPlayers = new HashMap<>();

		AudioSourceManagers.registerRemoteSources(manager);
	}

//	@Command(id = "Play", aliases = {"play", "resume"}, help = "music.play.help")
//	public void play(@Channels(ChannelType.TEXT) JDACEvent event) {
//		SoundPlayer player = getAudioPlayer(event);
//		Guild guild = event.getSource().getGuild();
//		boolean inVoice = guild.getSelfMember().getVoiceState().inVoiceChannel();
//
//		if (!inVoice)
//			event.send("music.play.not_in_voice");
//
//		else if (player.isIdle())
//			event.send("music.play.empty_queue");
//
//		else if (player.play())
//			event.send("music.play.resume");
//
//		else
//			event.send("music.play.playing");
//	}
//
//	@Command(id = "music.pause.name", aliases = {"pause", "stop"}, help = "music.pause.help")
//	public String pause(@Channels(ChannelType.TEXT) JDACEvent event) {
//		SoundPlayer player = getAudioPlayer(event);
//		Guild guild = event.getSource().getGuild();
//		boolean inVoice = guild.getSelfMember().getVoiceState().inVoiceChannel();
//
//		if (!inVoice || player.isIdle())
//			return "music.pause.not_playing";
//
//		if (!player.pause())
//			return "music.pause.not_playing";
//
//		return "music.pause.paused";
//	}
//
//	@Command(id = "music.queue.title", aliases = {"queue", "playing", "np"}, help = "music.queue.help")
//	public Object queue(@Channels(ChannelType.TEXT) JDACEvent event) throws IOException {
//		return queue(event, 0);
//	}
//
//	public Object queue(@Channels(ChannelType.TEXT) JDACEvent event, int offset) throws IOException {
//		SoundPlayer player = getAudioPlayer(event);
//		AudioTrack playing = player.getPlayingTrack();
//		List<AudioTrack> queue = player.getQueue();
//
//		if (player.isIdle())
//			return "music.queue.not_playing";
//
//		EmbedBuilder builder = new EmbedBuilder();
//
//		if (playing != null) {
//			AudioTrackInfo info = playing.getInfo();
//			String position = info.isStream ? "**\\\uD83D\uDD34Live**" : YouTubeService.toYouTubeTimeFormat(TimeUnit.MILLISECONDS, playing.getPosition(), playing.getDuration());
//			String title = formTitle(info.author, info.title, info.uri);
//
//			if (!queue.isEmpty())
//				title += "\n_ _";
//
//			builder.setThumbnail(youtube.getChannelThumbnail(info.identifier, true));
//			builder.addField("music.queue.now_playing | " + position, title, true);
//		}
//
//		if (!queue.isEmpty()) {
//			StringJoiner joiner = new StringJoiner("\n");
//
//			for (int i = 0; i < queue.size() && i < 8; i++) {
//				int position = i + offset;
//
//				if (position > queue.size() - 1)
//					position -= queue.size();
//
//				AudioTrack track = queue.get(position);
//				AudioTrackInfo info = track.getInfo();
//				String title = String.format("%s %s", NUMBERS[i], formTitle(info.author, info.title, info.uri));
//
//				if (joiner.length() + title.length() <= MessageEmbed.VALUE_MAX_LENGTH)
//					joiner.add(title);
//				else
//					break;
//			}
//
//			builder.addField("Queue", joiner.toString(), true);
//			builder.setFooter(String.format("music.queue.total_tracks", queue.size()), null);
//		}

//		event.addReaction("⬅", "1⃣", "2⃣","3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣", "➡");

//		if (offset == 0)
//			event.storeObject("offset", offset);

//		return builder;
//	}

//	@Reaction(id = 101, emotes = {"1⃣", "2⃣","3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣"})
//	public Object setTrack(ReactionEvent event) throws IOException {
//		SoundPlayer player = getAudioPlayer(event);
//		int position = 0;
//
//		switch (event.getReactionAddEvent().getReactionEmote().getName()) {
//			case "1⃣": position = 0; break;
//			case "2⃣": position = 1; break;
//			case "3⃣": position = 2; break;
//			case "4⃣": position = 3; break;
//			case "5⃣": position = 4; break;
//			case "6⃣": position = 5; break;
//			case "7⃣": position = 6; break;
//			case "8⃣": position = 7; break;
//		}
//
//		List<AudioTrack> queue = player.getQueue();
//
//		int offset = (int)event.getReactionRecord().getObject("offset");
//		position += offset;
//
//		if (queue.size() > position) {
//			player.getPlayer().playTrack(player.getQueue().remove(position));
//			return queue(event, offset);
//		}
//
//		return null;
//	}

//	@Reaction(id = 101, emotes = {"⬅", "➡"})
//	public Object nextOrPreviousPage(ReactionEvent event) throws IOException {
//		ReactionRecord record = event.getReactionRecord();
//		SoundPlayer player = getAudioPlayer(event);
//		List<AudioTrack> queue = player.getQueue();
//		int offset = (int)record.getObject("offset");
//
//		if (event.getReactionAddEvent().getReactionEmote().getName().equals("⬅")) {
//			offset -= 8;
//
//			if (offset < 0)
//				offset = queue.size() - (-offset);
//		} else {
//			offset += 8;
//
//			if (offset > queue.size())
//				offset -= queue.size();
//		}
//
//		record.storeObject("offset", offset);
//		return queue(event, offset);
//	}

//	@Command(id = "music.add.name", aliases = {"add", "append"}, help = "music.add.help")
//	@Param(id = "common.query", help = "music.param.query.help")
////	@Emoji(emotes = "\uD83D\uDD01", help = "music.emote.repeat.help")
//	public Object addTrack(@Channels(ChannelType.TEXT) JDACEvent event, String query) throws IOException {
//		if (!joinChannel(event))
//			return null;
//
////		event.addReaction("\uD83D\uDD01");
////		event.storeObject("query", query);
//
//		return handleSongAdded(event, query, false);
//	}

//	@Reaction(id = 100, emotes = "\uD83D\uDD01")
//	public Object addAgain(ReactionEvent event) throws IOException {
//		if (!joinChannel(event))
//			return null;
//
//		return handleSongAdded(event, (String)event.getReactionRecord().getObject("query"), false);
//	}

//	@Command(id = "music.insert.name", aliases = {"insert", "prepend"}, help = "music.insert.help")
//	@Param(id = "query", help = "music.param.query.help")
//	public Object insertTrack(@Channels(ChannelType.TEXT) JDACEvent event, String query) throws IOException {
//		if (!joinChannel(event))
//			return null;
//
//		return handleSongAdded(event, query, true);
//	}

//	@Command(name = "music.remove.name", aliases = {"remove", "rid"}, help = "music.remove.help")
//	@Param(name = "music.param.position.name", help = "music.param.position.help")
//	public void removeTrack(MessageEvent event, String id) {
//		getPlayer(event).removeTrack(id);
//	}
//
//	@Command(name = "music.skip.title", aliases = "skip", help = "music.skip.help")
//	public void skipTrack(MessageEvent event) {
//
//	}
//
//	@Command(name = "music.clear.title", aliases = {"clear", "prune", "purge"}, help = "music.clear.help")
//	public void clearPlaylist(MessageEvent event) {
//		getPlayer(event).clearPlaylist();
//	}
//
//	@Command(name = "music.shuffle.title", aliases = {"shuffle", "scramble"}, help = "music.shuffle.help")
//	public void shuffleQueue(MessageEvent event) {
//		getPlayer(event).shuffle();
//	}
//
//	@Command(name = "music.repeat.title", aliases = "repeat", help = "music.repeat.help")
//	public void repeatTrack(MessageEvent event) {
//
//	}
//
//	@Command(name = "music.position.title", aliases = {"position", "pos", "time"}, help = "music.position.help")
//	@Param(name = "common.time", help = "The time to move to, try '@Me help time' for format info.")
//	public void setTime(MessageEvent event, Duration time) {
//
//	}
//
//	@Command(name = "Fast Forward", aliases = {"forward", "fastforward", "ff"}, help = "Fastforward by a set amount of time.")
//	@Param(name = "common.time", help = "The time to move forward by, try '@Me help time' for format info.")
//	public void fastForward(MessageEvent event, Duration time) {
//
//	}
//
//	@Command(name = "Rewind", aliases = {"rewind", "backward", "rw"}, help = "Rewind by a set amount of time.")
//	@Param(name = "time", help = "The time to move rewind by, try '@Me help time' for format info.")
//	public void rewind(MessageEvent event, Duration time) {
//
//	}
//
//	@Command(name = "Volume", aliases = {"volume", "vol", "v"}, help = "Set the volume of the audioplayer.")
//	@Param(name = "volume", help = "The volume to change the audioplayer too.")
//	public void setVolume(MessageEvent event, @Limit(min = 0, max = 150) int volume) {
//		getPlayer(event);
//	}
//
//	@Command(name = "Leave", aliases = {"leave"}, help = "Stops playing music and leaves if applicable.")
//	public void leaveChannel(MessageEvent event) {
//
//	}
//
//    @Elevated
//	@Command(name = "Default Playlist", aliases = "default", help = "Specify the default track or playlist to add on join.")
//	public void defaultList(MessageEvent event) {
//
//	}

//	@Command(id = "Sync Channel Name with Track", aliases = {"sync", "rename"}, help = "Should Alexis append the currently playing track to the current voice channel's name?")
//    @Param(id = "toggle", help = "True or false, should this be enabled or not?")
//	public String channelName(
//		@Channels(ChannelType.TEXT) @Permissions(Permission.MANAGE_CHANNEL) @Elevated JDACEvent event,
//		boolean enable
//	) {
//        Guild guild = event.getSource().getGuild();
//
//	    GuildData data = GuildData.query(guild.getIdLong());
//		MusicSettings settings = data.getSettings().getMusicSettings();
//        boolean sync = settings.getSyncChannelName();
//
//	    if (sync == enable) {
//	        if (enable)
//	            return "That's already enabled, just check the channel name next time you're playing music!";
//	        else
//	            return "That's already disabled, I haven't been changing the channel name anyways.";
//        }
//
//        settings.setSyncChannelName(enable);
//		data.commit();
//
//		return "You've succesfully set the nickname sync setting to " + enable + "!";
//	}
//
//	/**
//	 * Handle a song request, this will add a song to the queue based on if
//	 * the user performed the add or insert command. Add will add the song to the
//	 * end of the queue, while insert will insert the command into the front of the
//	 * queue. <br>
//	 * If and only if the command is succesful and a song is found, we delete the users message
//	 * if we have permission to do so; this cleans up the channel from embed spam.
//	 *
//	 * @param event The event that trigged this commands execution.
//	 * @param query The url or search term, if this is not a url or identifiable id,
//	 * we search YouTube instead.
//	 * @param insert If the song(s) should be added to the end, or inserted to the front.
//	 * @return The reply for the user.
//	 * @throws IOException
//	 */
//	private Object handleSongAdded(JDACEvent event, String query, boolean insert) throws IOException {
//		SoundPlayer player = getAudioPlayer(event);
//		SoundLoader loader = player.add(query, insert);
//		List<AudioTrack> tracks = loader.getTracks();
//		AudioPlaylist playlist = loader.getPlaylist();
//
//		if (tracks.isEmpty())
//			return "Sorry, I couldn't find any results with your input.";
//
//		String method = insert ? "Inserted" : "Added";
//		EmbedBuilder builder = new EmbedBuilder();
//
//		if (playlist != null) {
//			AudioTrack single = tracks.get(0);
//			AudioTrackInfo info = single.getInfo();
//
//			builder.setTitle("Song " + method);
//			builder.setDescription(event.asMessageRecieved().getMessage().getAuthor().getAsMention() + " has " + method.toLowerCase() + " the song ");
//			builder.appendDescription(formTitle(info.author, info.title, info.uri, false));
//
//			builder.appendDescription(".");
//			builder.setThumbnail(youtube.getChannelThumbnail(single.getIdentifier(), true));
//		} else {
//			builder.setTitle("Playlist " + method);
//			builder.setDescription("All songs from the playlist ");
//			builder.appendDescription(playlist.getName());
//			builder.appendDescription(" have been " + method.toLowerCase() + " to the queue.");
//			builder.setFooter("The queue now has " + player.getQueue().size() + " songs.", null);
//		}
//
//		event.delete();
//		return builder;
//	}
//
//	/**
//	 * Attempts to join the voice channel the user is in, or if it fails
//	 * for whatever reason, prints the reason. <br>
//	 * <br>
//	 * This command returns a boolean to dictate if processing should continue,
//	 * this is because if the bot was already in the same channel as the user, that's
//	 * perfectly fine, however if the use not in the voice channel performs the command
//	 * we should fail and back out.
//	 *
//	 * @param event The message event containing the user and guild.
//	 * @return If to continue processing this command.
//	 */
//	private boolean joinChannel(JDACEvent event) {
//		Message message = event.asMessageRecieved().getMessage();
//		Member member = message.getMember();
//		VoiceChannel memberChannel = member.getVoiceState().getChannel();
//
//		AudioManager audioManager = message.getGuild().getAudioManager();
//		VoiceChannel botChannel = audioManager.getConnectedChannel();
//
//		if (memberChannel == null) {
//			event.send("You're not in a voice channel, {$user.name}! Join one and try again, I'll follow once you've added a track or playlist.");
//			return false;
//		}
//
//		if (botChannel != memberChannel) {
//			if (!memberChannel.getGuild().getSelfMember().hasPermission(memberChannel, Permission.VOICE_SPEAK)) {
//				boolean power = member.hasPermission(memberChannel, Permission.MANAGE_ROLES);
//
//				event.send("I lack permision to speak in the voice channel you're in. Could you {!$power}['ask someone with the `Manage Roles` permission to ']grant me the `Voice Speak` permission?");
//				return false;
//			}
//		}
//
//		if (audioManager.isConnected() && memberChannel != botChannel) {
//			long users = botChannel.getMembers().size();
//			long nonBotUsers = botChannel.getMembers().stream().filter(m -> !m.getUser().isBot()).count();
//			String botChannelName = botChannel.getName();
//
//			if (users == 0)
//				event.send("Sure, I'll move over from `" + botChannelName + "` since it's empty anyways.");
//
//			else if (nonBotUsers == 0)
//				event.send("Sure, I'll move over from `" + botChannelName + "` since there are only bots there anyways.");
//
//			else if (member.hasPermission(memberChannel, Permission.VOICE_MOVE_OTHERS) || member.hasPermission(memberChannel, Permission.MANAGE_SERVER))
//				event.send(member.getAsMention() + " is stealing me from `" + botChannelName + "`! DX");
//
//			else {
//				event.send("Sorry, I'm with other users! You could convince someone with manage server permission to make me move though!");
//				return false;
//			}
//		}
//
//		audioManager.openAudioConnection(memberChannel);
//		SoundPlayer guildPlayer = getAudioPlayer(event);
//
//		if (audioManager.getSendingHandler() == null)
//			audioManager.setSendingHandler(new SoundSendHandler(guildPlayer.getPlayer()));
//
//		guildPlayer.setChannel(event.getSource().getTextChannel().getIdLong());
//		return true;
//	}
//
//	private String formTitle(String author, String title, String url) {
//		return formTitle(author, title, url, true);
//	}
//
//	/**
//	 * Form the title of the track to display to users in the queue embed.
//	 * This title is shrunk down to {@link #LINE_MAX_LENGTH} characters to try
//	 * ensure the title only consumed a single line in the embed for aesthetics.
//	 * Due to an issue with incomplete square brackets, all sqaure brackets are replaced
//	 * with curly brackets for display to avoid breaking the markdown.
//	 *
//	 * @param author The author of the track.
//	 * @param title The name of the track.
//	 * @param url The URL to the track.
//	 * @return A markdown friendly URL to display this track title to users.
//	 */
//
//	private String formTitle(String author, String title, String url, boolean shorten) {
//		String result = title + " - " + author;
//
//		if (shorten) {
//			if (result.length() > LINE_MAX_LENGTH)
//				result = result.substring(0, LINE_MAX_LENGTH - 3) + "...";
//		}
//
//		result = result.replace("[", "{").replace("]", "}");
//		result = Md.a(result, url);
//		return result;
//	}
//
//	/**
//	 * Obtain the {@link SoundPlayer} instance associated with the guild
//	 * that spawned this event.
//	 *
//	 * @param event The message event which was created from a command.
//	 * @return The GuildPlayer from our guild player map, or a new one if new
//	 * instance existed for this guild yet.
//	 */
//
//	private SoundPlayer getAudioPlayer(JDACEvent event) {
//		GenericMessageEvent source = event.getSource();
//		long id = source.getGuild().getIdLong();
//
//		if (!guildPlayers.containsKey(id))
//			guildPlayers.put(id, new SoundPlayer(source.getJDA(), manager));
//
//		return guildPlayers.get(id);
//	}
}
