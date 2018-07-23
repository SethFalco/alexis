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

    public String getSecret() {
        return secret;
    }

    public String getTag() {
        return tag;
    }

    public AmazonEndpoint getEndpoint() {
        return endpoint;
    }
}
