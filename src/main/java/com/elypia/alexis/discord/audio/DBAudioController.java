package com.elypia.alexis.discord.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class DBAudioController extends AudioController {


    public DBAudioController(GuildAudioPlayer guildPlayer) {
        super(guildPlayer);
    }

    @Override
    public void onPlay(AudioTrack track) {

    }

    @Override
    public void add(AudioTrack track, int position) {

    }

    @Override
    public AudioTrack remove(String string) {
        return null;
    }

    @Override
    public void shuffle() {

    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public List<AudioTrack> getTracks() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }


    @Override
    public GuildAudioPlayer getPlayer() {
        return null;
    }

    @Override
    public void setPlayer(GuildAudioPlayer player) {

    }

    @Override
    public TextChannel getChannel() {
        return null;
    }

    @Override
    public void setChannel(TextChannel channel) {

    }
}
