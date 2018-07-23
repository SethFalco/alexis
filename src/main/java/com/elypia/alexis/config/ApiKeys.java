package com.elypia.alexis.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiKeys {

    @SerializedName("osu")
    private String osu;

    @SerializedName("twitch")
    private String twitch;

    @SerializedName("google")
    private String google;

    @SerializedName("steam")
    private String steam;

    @SerializedName("cleverbot")
    private String cleverbot;

    @SerializedName("amazon")
    private List<AmazonDetails> amazonDetails;

    public String getOsu() {
        return osu;
    }

    public String getTwitch() {
        return twitch;
    }

    public String getGoogle() {
        return google;
    }

    public String getSteam() {
        return steam;
    }

    public String getCleverbot() {
        return cleverbot;
    }

    public List<AmazonDetails> getAmazonDetails() {
        return amazonDetails;
    }
}
