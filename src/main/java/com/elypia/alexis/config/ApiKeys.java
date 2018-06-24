package com.elypia.alexis.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiKeys {

    @SerializedName("mal")
    private String myAnimeList;

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

    public String getMyAnimeList() {
        return myAnimeList;
    }

    public void setMyAnimeList(String myAnimeList) {
        this.myAnimeList = myAnimeList;
    }

    public String getOsu() {
        return osu;
    }

    public void setOsu(String osu) {
        this.osu = osu;
    }

    public String getTwitch() {
        return twitch;
    }

    public void setTwitch(String twitch) {
        this.twitch = twitch;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getSteam() {
        return steam;
    }

    public void setSteam(String steam) {
        this.steam = steam;
    }

    public String getCleverbot() {
        return cleverbot;
    }

    public void setCleverbot(String cleverbot) {
        this.cleverbot = cleverbot;
    }

    public List<AmazonDetails> getAmazonDetails() {
        return amazonDetails;
    }

    public void setAmazonDetails(List<AmazonDetails> amazonDetails) {
        this.amazonDetails = amazonDetails;
    }
}
