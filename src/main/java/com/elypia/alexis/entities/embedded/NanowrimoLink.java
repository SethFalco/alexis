package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

@Embedded
public class NanowrimoLink {

    @Property("username")
    private String username;

    @Property("secret")
    private String secret;

    @Property("is_private")
    private boolean isPrivate;

    public NanowrimoLink() {
        // Do nothing
    }

    public NanowrimoLink(String username, String secret, boolean isPrivate) {
        this.username = username;
        this.secret = secret;
        this.isPrivate = isPrivate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
