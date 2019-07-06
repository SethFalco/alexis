package com.elypia.alexis.config;

import com.electronwill.nightconfig.core.conversion.Path;

import javax.inject.Singleton;

@Singleton
public class ApiCredentials {

    /** The path to the Service Account Key JSON file. */
    @Path("google")
    private String google;

    /** The osu! API key. */
    @Path("osu")
    private String osu;

    /** Twitch API credentials. */
    @Path("twitch")
    private TwitchConfig twitchConfig;

    /** Steam API key. */
    @Path("steam")
    private String steam;

    /** Cleverbot API key. */
    @Path("cleverbot")
    private String cleverbot;

    public String getGoogle() {
        return google;
    }

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
