package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.annotation.BeforeAny;
import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.audio.AudioController;
import com.elypia.alexis.discord.audio.AudioPlayerSendHandler;
import com.elypia.alexis.discord.audio.GuildAudioPlayer;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.CommandHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.elypia.elypiai.utils.ElyUtils.makeMarkdown;

@Module (
	aliases = {"Music", "m"},
	help = "Music player to listen to music to your hearts content in guilds!",
	scope = ChannelType.TEXT
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

	@BeforeAny (exclusions = {"help", "leave"})
	public boolean beforeAny(CommandEvent event) {
		Guild guild = event.getGuild();
		AudioManager audioManager = guild.getAudioManager();
		VoiceChannel channel = event.getMember().getVoiceState().getChannel();

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

	@Command (
		aliases = {"play", "resume"},
		help = "Play if the music if it's paused."
	)
	public void playPlayer(CommandEvent event) {
		getPlayer(event).play();
	}

	@Command (
		aliases = {"pause", "stop"},
		help = "Pause the music if it's playing."
	)
	public void pausePlayer(CommandEvent event) {
		getPlayer(event).pause();
	}

	@Command (
		aliases = {"queue", "playing", "np"},
		help = "Display the playing track and queue."
	)
	public void displayQueue(CommandEvent event) {
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
			String name = makeMarkdown(String.format("%s - %s", info.author, info.title), info.uri);
			name = String.format("%s `%s`", name, position);
			builder.addField("Now Playing", name, true);
		}

		if (!tracks.isEmpty()) {
			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 0; i < tracks.size(); i++) {
				AudioTrack t = tracks.get(i);
				AudioTrackInfo info = t.getInfo();
				String text = String.format("%s - %s", info.author, info.title);
				text = makeMarkdown(text, info.uri);
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

	@Command (
		aliases = {"add", "append"},
		help = "Add a track to the end of the playlist.",
		params = {
			@Parameter (
				param = "query",
				help = "The URL for the audio or what to search for on YouTube!",
				type = String.class
			)
		}
	)
	public void addTrack(CommandEvent event) {
		String[] params = event.getParams();
		getPlayer(event).addTrack(params[0]);
	}

	@Command (
		aliases = {"insert", "prepend"},
		help = "Insert a track to the start of the queue.",
		params = {
			@Parameter (
				param = "query",
				help = "The URL for the audio or what to search for on YouTube!",
				type = String.class
			)
		}
	)
	public void insertTrack(CommandEvent event) {
		String[] params = event.getParams();
		getPlayer(event).insertTrack(params[0]);
	}

	@Command (
		aliases = {"remove", "rid"},
		help = "Remove a track from the playlist.",
		params = {
			@Parameter (
				param = "identifier",
				help = "A way to identify the track.",
				type = String.class
			)
		}
	)
	public void removeTrack(CommandEvent event) {
		String[] params = event.getParams();
		getPlayer(event).removeTrack(params[0]);
	}

	@Command (
		aliases = "skip",
		help = "Skip the currently playing track."
	)
	public void skipTrack(CommandEvent event) {

	}

	@Command (
		aliases = {"clear", "prune", "purge"},
		help = "Remove all tracks for the queue."
	)
	public void clearPlaylist(CommandEvent event) {
		getPlayer(event).clearPlaylist();
	}

	@Command (
		aliases = {"shuffle", "scramble"},
		help = "Shuffle the songs in the queue in a random order."
	)
	public void shuffleQueue(CommandEvent event) {
		getPlayer(event).shuffle();
	}

	@Command (
		aliases = "repeat",
		help = "Repeat a track or playlist, once or many times."
	)
	public void repeatTrack(CommandEvent event) {

	}

	@Command (
		aliases = "time",
		help = "Set the time to go to in the current song.",
		params = {
			@Parameter (
				param = "time",
				help = "The time to move to, try '@Me help time' for format info.",
				type = String.class
			)
		}
	)
	public void setTime(CommandEvent event) {

	}

	@Command (
		aliases = {"forward", "fastforward", "ff"},
		help = "Fastforward by a set amount of time.",
		params = {
			@Parameter (
				param = "time",
				help = "The time to move forward by, try '@Me help time' for format info.",
				type = String.class
			)
		}
	)
	public void fastForward(CommandEvent event) {

	}

	@Command (
		aliases = {"rewind", "backward", "rw"},
		help = "Rewind by a set amount of time.",
		params = {
			@Parameter (
				param = "time",
				help = "The time to move rewind by, try '@Me help time' for format info.",
				type = String.class
			)
		}
	)
	public void rewind(CommandEvent event) {

	}

	@Command (
		aliases = {"volume", "vol", "v"},
		help = "Set the volume of the audioplayer.",
		params = {
			@Parameter (
				param = "volume",
				help = "The volume to change the audioplayer too.",
				type = Integer.class
			)
		}
	)
	public void setVolume(CommandEvent event) {

	}

	@Command (
		aliases = {"leave"},
		help = "Stops playing music and leaves if applicable."
	)
	public void leaveChannel(CommandEvent event) {

	}

	@Command (
		aliases = "default",
		help = "Specify the default track or playlist to add on join.",
		permissions = Permission.MANAGE_SERVER
	)
	public void defaultList(CommandEvent event) {

	}

	@Command (
		aliases = {"nickname", "nicksync", "ns"},
		help = "Should Alexis append her name with the current song?",
		permissions = Permission.MANAGE_SERVER
	)
	public void nicknameSync(CommandEvent event) {

	}

	private GuildAudioPlayer getPlayer(CommandEvent event) {
		return guildPlayers.get(event.getGuild().getIdLong());
	}
}
