package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.audio.*;
import com.elypia.alexis.audio.controllers.impl.AudioController;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.utils.Markdown;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.time.Instant;
import java.util.*;

@Scope(ChannelType.TEXT)
@Module(
	name = "Music Player",
	aliases = {"music", "m"},
	description = "Music player to listen to music to your hearts content in guilds!"
)
public class MusicHandler extends CommandHandler {

	private Class<? extends AudioController> clazz;
	private DefaultAudioPlayerManager manager;
	private Map<Long, GuildAudioPlayer> guildPlayers;

	public MusicHandler(Class<? extends AudioController> clazz) {
		this.clazz = clazz;

		guildPlayers = new HashMap<>();
		manager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(manager);
	}

	private boolean beforeAny(MessageEvent event) {
		GenericMessageEvent e = event.getMessageEvent();
		Guild guild = e.getGuild();
		AudioManager audioManager = guild.getAudioManager();
		VoiceChannel channel = event.getMessage().getMember().getVoiceState().getChannel();

		if (channel == null)
			return false;
		else
			audioManager.openAudioConnection(channel);

		GuildAudioPlayer guildPlayer;

		if (guildPlayers.containsKey(guild.getIdLong())) {
			guildPlayer = guildPlayers.get(guild.getIdLong());
		} else {
			guildPlayer = new GuildAudioPlayer(clazz, manager, event);
			guildPlayers.put(guild.getIdLong(), guildPlayer);
		}

		if (audioManager.getSendingHandler() == null)
			audioManager.setSendingHandler(new AudioPlayerSendHandler(guildPlayer.getPlayer()));

		guildPlayer.setChannel(event.getMessageEvent().getTextChannel());
		return true;
	}

	@Command(name = "Play", aliases = {"play", "resume"}, help = "Play if the music if it's paused.")
	public void playPlayer(MessageEvent event) {
		beforeAny(event);
		getPlayer(event).play();
	}

	@Command(name = "Pause", aliases = {"pause", "stop"}, help = "Pause the music if it's playing.")
	public void pausePlayer(MessageEvent event) {
		getPlayer(event).pause();
	}

	@Command(name = "Queue", aliases = {"queue", "playing", "np"}, help = "Display the playing track and queue.")
	public void displayQueue(MessageEvent event) {
		GuildAudioPlayer player = getPlayer(event);
		AudioTrack track = player.getPlayingTrack();
		List<AudioTrack> tracks = player.getTracks();

		if (track == null && tracks.isEmpty()) {
			event.reply("I'm not playing anything at the moment. ^-^'");
			return;
		}

		EmbedBuilder builder = new EmbedBuilder();

		builder.setTitle("Queue");
		builder.setDescription(String.format("There are %,d tracks in the queue!", tracks.size()));

		if (track != null) {
			AudioTrackInfo info = track.getInfo();
			String position = info.isStream ? "∞" : String.format("%,d/%,ds", (track.getPosition() / 1000), (track.getDuration() / 1000));
			String name = Markdown.a(String.format("%s - %s", info.author, info.title), info.uri);
			name = String.format("%s `%s`", name, position);
			builder.addField("Now Playing", name, true);
		}

		if (!tracks.isEmpty()) {
			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 0; i < tracks.size(); i++) {
				AudioTrack t = tracks.get(i);
				AudioTrackInfo info = t.getInfo();
				String text = String.format("%s - %s", info.author, info.title);
				text = Markdown.a(text, info.uri);
				text = String.format("**%d.** %s", i + 1, text);

				if (stringBuilder.length() + text.length() <= MessageEmbed.VALUE_MAX_LENGTH)
					stringBuilder.append(text);
				else
					break;
			}

			builder.addField("Queue", stringBuilder.toString(), true);
		}

		event.reply(builder);
	}

	@Command(name = "Add to Queue", aliases = {"add", "append"}, help = "Add a track to the end of the playlist.")
	@Param(name = "query", help = "The URL for the audio or what to search for on YouTube!")
	public void addTrack(MessageEvent event, String query) {
		getPlayer(event).addTrack(query);
	}

	@Command(name = "Insert into Queue", aliases = {"insert", "prepend"}, help = "Insert a track to the start of the queue.")
	@Param(name = "query", help = "The URL for the audio or what to search for on YouTube!")
	public void insertTrack(MessageEvent event, String query) {
		getPlayer(event).insertTrack(query);
	}

	@Command(name = "Remove Track from Queue", aliases = {"remove", "rid"}, help = "Remove a track from the playlist.")
	@Param(name = "position", help = "The track position to remove.")
	public void removeTrack(MessageEvent event, String id) {
		getPlayer(event).removeTrack(id);
	}

	@Command(name = "Skip Track", aliases = "skip", help = "Skip the currently playing track.")
	public void skipTrack(MessageEvent event) {

	}

	@Command(name = "Clear Queue", aliases = {"clear", "prune", "purge"}, help = "Remove all tracks for the queue.")
	public void clearPlaylist(MessageEvent event) {
		getPlayer(event).clearPlaylist();
	}

	@Command(name = "Shuffle Queue", aliases = {"shuffle", "scramble"}, help = "Shuffle the songs in the queue in a random order.")
	public void shuffleQueue(MessageEvent event) {
		getPlayer(event).shuffle();
	}

	@Command(name = "Repeat Track", aliases = "repeat", help = "Repeat a track or playlist, once or many times.")
	public void repeatTrack(MessageEvent event) {

	}

	@Command(name = "Set Track Position", aliases = {"position", "pos", "time"}, help = "Set the time to go to in the current song.")
	@Param(name = "time", help = "The time to move to, try '@Me help time' for format info.")
	public void setTime(MessageEvent event, Instant time) {

	}

	@Command(name = "Fast Forward", aliases = {"forward", "fastforward", "ff"}, help = "Fastforward by a set amount of time.")
	@Param(name = "time", help = "The time to move forward by, try '@Me help time' for format info.")
	public void fastForward(MessageEvent event) {

	}

	@Command(name = "Rewind", aliases = {"rewind", "backward", "rw"}, help = "Rewind by a set amount of time.")
	@Param(name = "time", help = "The time to move rewind by, try '@Me help time' for format info.")
	public void rewind(MessageEvent event, Instant time) {

	}

	@Command(name = "Volume", aliases = {"volume", "vol", "v"}, help = "Set the volume of the audioplayer.")
	@Param(name = "volume", help = "The volume to change the audioplayer too.")
	public void setVolume(MessageEvent event, @Limit(min = 0, max = 150) int volume) {

	}

	@Command(name = "Leave", aliases = {"leave"}, help = "Stops playing music and leaves if applicable.")
	public void leaveChannel(MessageEvent event) {

	}

	@Command(name = "Default Playlist", aliases = "default", help = "Specify the default track or playlist to add on join.")
	@Permissions(Permission.MANAGE_SERVER)
	public void defaultList(MessageEvent event) {

	}

	@Command(name = "Change Nickname", aliases = {"nickname", "nicksync", "ns"}, help = "Should Alexis append her name with the current song?")
	@Permissions(Permission.MANAGE_SERVER)
	public void nicknameSync(MessageEvent event) {

	}

	private GuildAudioPlayer getPlayer(MessageEvent event) {
		return guildPlayers.get(event.getMessageEvent().getGuild().getIdLong());
	}
}
