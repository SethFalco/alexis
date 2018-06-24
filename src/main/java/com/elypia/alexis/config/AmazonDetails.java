package com.elypia.alexis.config;

import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import com.google.gson.annotations.SerializedName;

public class AmazonDetails {

    @SerializedName("key")
    private String key;

    @SerializedName("secret")
    private String secret;

    @SerializedName("tag")
    private String tag;

    @SerializedName("endpoint")
    private AmazonEndpoint endpoint;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public AmazonEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(AmazonEndpoint endpoint) {
        this.endpoint = endpoint;
    }
}
