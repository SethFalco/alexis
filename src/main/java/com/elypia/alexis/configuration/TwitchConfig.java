package com.elypia.alexis.configuration;

import com.electronwill.nightconfig.core.conversion.Path;

import javax.inject.Singleton;

@Singleton
public class TwitchConfig {

    @Path("key")
    private String key;

    @Path("secret")
    private String secret;

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
}
