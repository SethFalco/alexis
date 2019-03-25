package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

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
