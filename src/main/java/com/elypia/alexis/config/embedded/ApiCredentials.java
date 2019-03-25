package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

import java.util.List;

public class ApiCredentials {

    @Path("osu")
    private String osu;

    @Path("twitch")
    private TwitchConfig twitchConfig;

    @Path("steam")
    private String steam;

    @Path("cleverbot")
    private String cleverbot;

    public String getOsu() {
        return osu;
    }

    public TwitchConfig getTwitchConfig() {
        return twitchConfig;
    }

    public String getSteam() {
        return steam;
    }

    public String getCleverbot() {
        return cleverbot;
    }
}
