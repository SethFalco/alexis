package com.elypia.alexis.audio.controllers.impl;

import com.elypia.alexis.audio.GuildAudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public interface IAudioController {

    void onPlay(AudioTrack track);

    void add(AudioTrack track, int position);

    AudioTrack remove(String string);

    void shuffle();

    boolean clear();

    List<AudioTrack> getTracks();

    int getSize();

    GuildAudioPlayer getPlayer();

    void setPlayer(GuildAudioPlayer player);

    TextChannel getChannel();

    void setChannel(TextChannel channel);
}
