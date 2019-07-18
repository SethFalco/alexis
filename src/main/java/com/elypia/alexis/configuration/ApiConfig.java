package com.elypia.alexis.configuration;

import com.electronwill.nightconfig.core.conversion.Path;

import javax.inject.Singleton;

@Singleton
public class ApiConfig {

    /** The path to the Service Account Key JSON file. */
    @Path("google")
    private String google;

    /** The osu! API key. */
    @Path("osu")
    private String osu;

    /** Steam API key. */
    @Path("steam")
    private String steam;

    /** Cleverbot API key. */
    @Path("cleverbot")
    private String cleverbot;

    /** Twitch API credentials. */
    @Path("twitch")
    private TwitchConfig twitchConfig;

    public String getGoogle() {
        return google;
    }

    public String getOsu() {
        return osu;
    }

    public String getSteam() {
        return steam;
    }

    public String getCleverbot() {
        return cleverbot;
    }

    public TwitchConfig getTwitch() {
        return twitchConfig;
    }
}
