package com.elypia.alexis.discord.audio.impl;

import com.elypia.alexis.discord.audio.GuildAudioPlayer;
import com.elypia.alexis.discord.audio.controllers.DBAudioController;
import com.elypia.alexis.discord.audio.controllers.LocalAudioController;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.JDA;

public abstract class AudioController implements IAudioController {

    protected JDA jda;
    protected GuildAudioPlayer guildPlayer;
    protected AudioPlayer player;

    protected AudioController(GuildAudioPlayer guildPlayer) {
        this.guildPlayer = guildPlayer;
        jda = guildPlayer.getJDA();
        player = guildPlayer.getPlayer();
    }

    public static AudioController getInstance(Class<? extends AudioController> clazz, GuildAudioPlayer guildPlayer) {
        if (clazz == DBAudioController.class)
            return new DBAudioController(guildPlayer);

        else if (clazz == LocalAudioController.class)
            return new LocalAudioController(guildPlayer);

        else
            throw new IllegalArgumentException("Not instance of Local or Database AudioController.");
    }
}
