package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.Embedded;

@Embedded
public class GreetingSetting {

    @Embedded("user")
    private MessageSettings user;

    @Embedded("bot")
    private MessageSettings bot;

    public MessageSettings getUser() {
        if (user == null)
            user = new MessageSettings();

        return user;
    }

    public void setUser(MessageSettings user) {
        this.user = user;
    }

    public MessageSettings getBot() {
        if (bot == null)
            bot = new MessageSettings();

        return bot;
    }

    public void setBot(MessageSettings bot) {
        this.bot = bot;
    }
}
