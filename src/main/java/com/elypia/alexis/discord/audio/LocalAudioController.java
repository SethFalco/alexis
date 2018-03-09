package com.elypia.alexis.discord.audio;

import com.elypia.elypiai.utils.ElyUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalAudioController extends AudioController {

    private long channel;
    private List<AudioTrack> tracks;

    public LocalAudioController(GuildAudioPlayer guildPlayer) {
        super(guildPlayer);

        tracks = new ArrayList<>();
    }

    @Override
    public void onPlay(AudioTrack track) {

    }

    @Override
    public void add(AudioTrack track, int position) {
        tracks.add(position, track);
    }

    @Override
    public AudioTrack remove(String string) {
        for (int i = 0; i < tracks.size(); i++) {
            AudioTrackInfo info = tracks.get(i).getInfo();

            if (string.equalsIgnoreCase(info.uri) || string.equalsIgnoreCase(info.title))
                return tracks.remove(i);
        }

        return null;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(tracks, ElyUtils.RAND);
    }

    @Override
    public boolean clear() {
        if (!tracks.isEmpty())
            return false;

        tracks.clear();
        return true;
    }

    @Override
    public List<AudioTrack> getTracks() {
        return tracks;
    }

    @Override
    public int getSize() {
        return tracks.size();
    }

    @Override
    public GuildAudioPlayer getPlayer() {
        return guildPlayer;
    }

    @Override
    public void setPlayer(GuildAudioPlayer player) {
        this.guildPlayer = player;
    }

    @Override
    public TextChannel getChannel() {
        return jda.getTextChannelById(channel);
    }

    @Override
    public void setChannel(TextChannel channel) {
        this.channel = channel.getIdLong();
    }
}
