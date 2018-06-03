package com.elypia.alexis.entities;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GreetingSetting {

    @Embedded("user")
    private MessageSettings user;

    @Embedded("bot")
    private MessageSettings bot;

    public MessageSettings getUser() {
        return user;
    }

    public void setUser(MessageSettings user) {
        this.user = user;
    }

    public MessageSettings getBot() {
        return bot;
    }

    public void setBot(MessageSettings bot) {
        this.bot = bot;
    }
}
