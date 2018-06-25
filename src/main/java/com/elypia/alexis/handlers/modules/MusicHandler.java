package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.audio.*;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.youtube.YouTubeHelper;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.*;
import com.elypia.elypiai.utils.math.MathUtils;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.AudioManager;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Scope(ChannelType.TEXT)
@Module(name = "Music Player", aliases = {"music", "m"}, description = "Music player to listen to music to your hearts content in guilds!")
public class MusicHandler extends CommandHandler {

	private static final int LINE_MAX_LENGTH = 55;

	/**
	 * This allows us to query YouTube for information on videos,
	 * channels or playlists.
	 */

	private YouTubeHelper youtube;

	/**
	 * The manager class which allows us to create new audio players
	 * (for each guild) and parse tracks as {@link AudioTrack}s.
	 */

	private DefaultAudioPlayerManager manager;

	/**
	 * A map of Guild IDs to audio players, as each guild owns their own
	 * independent music player seperate from others.
	 */

	private Map<Long, GuildAudioPlayer> guildPlayers;

	/**
	 * Pass the YouTubeHelper instance over, this is injecting
	 * so there is no need to create multiple instances of this whenever
	 * else YouTube requests may be required.
	 *
	 * @param youtube The YouTubeHelper for making HTTP requests to YouTube.
	 */

	public MusicHandler(YouTubeHelper youtube) {
		this.youtube = youtube;
		manager = new DefaultAudioPlayerManager();
		guildPlayers = new HashMap<>();

		AudioSourceManagers.registerRemoteSources(manager);
	}

	@Command(name = "Play", aliases = {"play", "resume"}, help = "Play if the music if it's paused.")
	public String play(MessageEvent event) {
		GuildAudioPlayer player = getAudioPlayer(event);

		if (!event.getMessageEvent().getGuild().getSelfMember().getVoiceState().inVoiceChannel())
			return "There is nothing to play though? Perhaps add a song instead, I'll join you as soon as you do!";

		else if (player.isIdle())
			return "There is nothing to play though? Perhaps add a song instead! I'll start playing as soon as you do!";

		else if (player.play())
			return "Resuming where you left off! ^-^";

		else
			return "I'm already playing music though! Maybe you've got me muted? T^T";
	}

	@Command(name = "Pause", aliases = {"pause", "stop"}, help = "Pause the music if it's playing.")
	public String pause(MessageEvent event) {
		GuildAudioPlayer player = getAudioPlayer(event);

		if (!event.getMessageEvent().getGuild().getSelfMember().getVoiceState().inVoiceChannel() || player.isIdle())
			return "There is nothing to pause though? Perhaps you meant to do that to another bot?";

		if (!player.pause())
			return "I'm already paused though? You'd have to teach me how to double pause before I could do that. :c";

		String prefix = confiler.getPrefix(event.getMessageEvent());
		return "I've paused the music now, feel free to do `" + prefix + "music play` whenever you want me to play again!";
	}

	@Command(name = "Queue", aliases = {"queue", "playing", "np"}, help = "Display the playing track and queue.")
	public Object queue(MessageEvent event) throws IOException {
		GuildAudioPlayer player = getAudioPlayer(event);
		AudioTrack playing = player.getPlayingTrack();
		List<AudioTrack> queue = player.getQueue();

		if (playing == null && queue.isEmpty())
			return "I'm not playing anything at the moment. ^-^'";

		EmbedBuilder builder = new EmbedBuilder();

		if (playing != null) {
			AudioTrackInfo info = playing.getInfo();
			String position = info.isStream ? "**\\\uD83D\uDD34Live**" : YouTubeHelper.toYouTubeTimeFormat(TimeUnit.MILLISECONDS, playing.getPosition(), playing.getDuration());
			String title = formTitle(info.author, info.title, info.uri);

			if (!queue.isEmpty())
				title += "\n_ _";

			builder.setThumbnail(youtube.getChannelThumbnail(info.identifier, true));
			builder.addField("Now Playing | " + position, title, true);
		}

		if (!queue.isEmpty()) {
			StringJoiner joiner = new StringJoiner("\n");

			for (int i = 0; i < queue.size() && i < 8; i++) {
				AudioTrack track = queue.get(i);
				AudioTrackInfo info = track.getInfo();
				String title = String.format(":%s: %s", MathUtils.asWritten(i + 1), formTitle(info.author, info.title, info.uri));

				if (joiner.length() + title.length() <= MessageEmbed.VALUE_MAX_LENGTH)
					joiner.add(title);
				else
					break;
			}

			builder.addField("Queue", joiner.toString(), true);
			builder.setFooter(String.format("There are %,d tracks in the queue!", queue.size()), null);
		}

		return builder;
	}

	@Command(name = "Add to Queue", aliases = {"add", "append"}, help = "Add a track to the end of the playlist.")
	@Param(name = "query", help = "The URL for the audio or what to search for on YouTubeHelper!")
	public Object addTrack(MessageEvent event, String query) throws IOException {
		if (!joinChannel(event))
			return null;

		return handleSongAdded(event, query, false);
	}

	@Command(name = "Insert into Queue", aliases = {"insert", "prepend"}, help = "Insert a track to the start of the queue.")
	@Param(name = "query", help = "The URL for the audio or what to search for on YouTubeHelper!")
	public Object insertTrack(MessageEvent event, String query) throws IOException {
		if (!joinChannel(event))
			return null;

		return handleSongAdded(event, query, true);
	}

//	@Command(name = "Remove Track from Queue", aliases = {"remove", "rid"}, help = "Remove a track from the playlist.")
//	@Param(name = "position", help = "The track position to remove.")
//	public void removeTrack(MessageEvent event, String id) {
//		getPlayer(event).removeTrack(id);
//	}
//
//	@Command(name = "Skip Track", aliases = "skip", help = "Skip the currently playing track.")
//	public void skipTrack(MessageEvent event) {
//
//	}
//
//	@Command(name = "Clear Queue", aliases = {"clear", "prune", "purge"}, help = "Remove all tracks for the queue.")
//	public void clearPlaylist(MessageEvent event) {
//		getPlayer(event).clearPlaylist();
//	}
//
//	@Command(name = "Shuffle Queue", aliases = {"shuffle", "scramble"}, help = "Shuffle the songs in the queue in a random order.")
//	public void shuffleQueue(MessageEvent event) {
//		getPlayer(event).shuffle();
//	}
//
//	@Command(name = "Repeat Track", aliases = "repeat", help = "Repeat a track or playlist, once or many times.")
//	public void repeatTrack(MessageEvent event) {
//
//	}
//
//	@Command(name = "Set Track Position", aliases = {"position", "pos", "time"}, help = "Set the time to go to in the current song.")
//	@Param(name = "time", help = "The time to move to, try '@Me help time' for format info.")
//	public void setTime(MessageEvent event, Instant time) {
//
//	}
//
//	@Command(name = "Fast Forward", aliases = {"forward", "fastforward", "ff"}, help = "Fastforward by a set amount of time.")
//	@Param(name = "time", help = "The time to move forward by, try '@Me help time' for format info.")
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

	@Elevated
	@Command(name = "Sync Nickname with Track", aliases = {"nickname", "nicksync", "ns"}, help = "Should Alexis append the currently playing track to her nickname?")
    @Param(name = "toggle", help = "True or false, should this be enabled or not?")
	public String nicknameSync(MessageEvent event, boolean enable) {
        Guild guild = event.getMessageEvent().getGuild();

	    GuildData data = GuildData.query(guild.getIdLong());
        boolean sync = data.getSettings().getMusicSettings().getSyncNickname();

	    if (sync == enable) {
	        if (enable)
	            return "That's already enabled, just check my nickname next time you're playing music!";
	        else
	            return "That's already disabled, I haven't been changing my nickname anyways.";
        }

        Member self = guild.getSelfMember();

	    if (enable && self.hasPermission(Permission.NICKNAME_CHANGE))
            return "That wouldn't work out right now since I don't have permission to change my nickname! Please grant me that permission then try again.";

		Datastore store = Alexis.getChatbot().getDatastore();

		UpdateOptions options = new UpdateOptions();
		options.upsert(true);

		Query<GuildData> query = store.createQuery(GuildData.class);
		query = query.filter("guild_id", event.getMessageEvent().getGuild().getIdLong());

		UpdateOperations<GuildData> update = store.createUpdateOperations(GuildData.class);
		update.set("settings.music.nickname_sync", enable);

		store.update(query, update, options);

		return "You've succesfully set the nickname sync setting to " + enable + "!";
	}

	/**
	 * Handle a song request, this will add a song to the queue based on if
	 * the user performed the add or insert command. Add will add the song to the
	 * end of the queue, while insert will insert the command into the front of the
	 * queue. <br>
	 * If and only if the command is succesful and a song is found, we delete the users message
	 * if we have permission to do so; this cleans up the channel from embed spam.
	 *
	 * @param event The event that trigged this commands execution.
	 * @param query The url or search term, if this is not a url or identifiable id,
	 * we search YouTube instead.
	 * @param insert If the song(s) should be added to the end, or inserted to the front.
	 * @return The reply for the user.
	 * @throws IOException
	 */

	private Object handleSongAdded(MessageEvent event, String query, boolean insert) throws IOException {
		GuildAudioPlayer player = getAudioPlayer(event);
		Tuple<AudioPlaylist, List<AudioTrack>> tuple = player.add(query, insert);

		if (tuple == null)
			return "Sorry, I couldn't find any results with your input.";

		event.tryDeleteMessage();

		String method = insert ? "Inserted" : "Added";
		AudioPlaylist playlist = tuple.getValueOne();
		List<AudioTrack> tracks = tuple.getValueTwo();
		AudioTrack single = null;

		if (playlist != null && playlist.getSelectedTrack() != null)
			single = playlist.getSelectedTrack();

		else if (tracks.size() == 1)
			single = tracks.get(0);

		EmbedBuilder builder = new EmbedBuilder();

		if (single != null) {
			AudioTrackInfo info = single.getInfo();

			builder.setTitle("Song " + method);
			builder.setDescription(event.getMessage().getAuthor().getAsMention() + " has " + method.toLowerCase() + " the song ");
			builder.appendDescription(formTitle(info.author, info.title, info.uri, false));

			if (playlist != null)
				builder.appendDescription(" from the playlist " + playlist.getName());

			builder.appendDescription(".");
			builder.setThumbnail(youtube.getChannelThumbnail(single.getIdentifier(), true));
		} else {
			builder.setTitle("Playlist " + method);
			builder.setDescription("All songs from the playlist ");
			builder.appendDescription(playlist.getName());
			builder.appendDescription(" have been " + method.toLowerCase() + " to the queue.");
			builder.setFooter("The queue now has " + player.getQueue().size() + " songs.", null);
		}

		return builder;
	}

	/**
	 * Attempts to join the voice channel the user is in, or if it fails
	 * for whatever reason, prints the reason. <br>
	 * <br>
	 * This command returns a boolean to dictate if processing should continue,
	 * this is because if the bot was already in the same channel as the user, that's
	 * perfectly fine, however if the use not in the voice channel performs the command
	 * we should fail and back out.
	 *
	 * @param event The message event containing the user and guild.
	 * @return If to continue processing this command.
	 */

	private boolean joinChannel(MessageEvent event) {
		Message message = event.getMessage();
		Member member = message.getMember();
		VoiceChannel memberChannel = member.getVoiceState().getChannel();

		AudioManager audioManager = message.getGuild().getAudioManager();
		VoiceChannel botChannel = audioManager.getConnectedChannel();

		if (!audioManager.isConnected() && memberChannel == null) {
			event.reply("You're not in a voice channel though? Join one and try again, I'll follow once you've added a track or playlist.");
			return false;
		}

		if (audioManager.isConnected() && memberChannel != botChannel) {
			long users = botChannel.getMembers().size();
			long nonBotUsers = botChannel.getMembers().stream().filter(m -> !m.getUser().isBot()).count();
			String botChannelName = botChannel.getName();

			if (users == 0)
				event.reply("Sure, I'll move over from `" + botChannelName + "` since it's empty anyways.");

			else if (nonBotUsers == 0)
				event.reply("Sure, I'll move over from `" + botChannelName + "` since there are only bots there anyways.");

			else if (member.hasPermission(memberChannel, Permission.MANAGE_SERVER))
				event.reply(member.getAsMention() + " is stealing me from `" + botChannelName + "`! DX");

			else {
				event.reply("Sorry, I'm with other users! You could convince someone with manage server permission to make me move though!");
				return false;
			}
		}

		audioManager.openAudioConnection(memberChannel);
		GuildAudioPlayer guildPlayer = getAudioPlayer(event);

		if (audioManager.getSendingHandler() == null)
			audioManager.setSendingHandler(new AudioPlayerSendHandler(guildPlayer.getPlayer()));

		guildPlayer.setChannel(event.getMessageEvent().getTextChannel().getIdLong());
		return true;
	}

	private String formTitle(String author, String title, String url) {
		return formTitle(author, title, url, true);
	}

	/**
	 * Form the title of the track to display to users in the queue embed.
	 * This title is shrunk down to {@link #LINE_MAX_LENGTH} characters to try
	 * ensure the title only consumed a single line in the embed for aesthetics.
	 * Due to an issue with incomplete square brackets, all sqaure brackets are replaced
	 * with curly brackets for display to avoid breaking the markdown.
	 *
	 * @param author The author of the track.
	 * @param title The name of the track.
	 * @param url The URL to the track.
	 * @return A markdown friendly URL to display this track title to users.
	 */

	private String formTitle(String author, String title, String url, boolean shorten) {
		String result = title + " - " + author;

		if (shorten) {
			if (result.length() > LINE_MAX_LENGTH)
				result = result.substring(0, LINE_MAX_LENGTH - 3) + "...";
		}

		result = result.replace("[", "{").replace("]", "}");
		result = Markdown.a(result, url);
		return result;
	}

	/**
	 * Obtain the {@link GuildAudioPlayer} instance associated with the guild
	 * that spawned this event.
	 *
	 * @param event The message event which was created from a command.
	 * @return The GuildPlayer from our guild player map, or a new one if new
	 * instance existed for this guild yet.
	 */

	private GuildAudioPlayer getAudioPlayer(MessageEvent event) {
		long id = event.getMessageEvent().getGuild().getIdLong();

		if (!guildPlayers.containsKey(id))
			guildPlayers.put(id, new GuildAudioPlayer(jda, manager));

		return guildPlayers.get(id);
	}
}
