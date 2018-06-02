package com.elypia.alexis.entities;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GreetingSettings {

    @Embedded("welcome.user")
    private MessageSettings userWelcome;

    @Embedded("welcome.bot")
    private MessageSettings botWelcome;

    @Embedded("farewell.user")
    private MessageSettings userFarewell;

    @Embedded("farewell.bot")
    private MessageSettings botFarewell;

    public MessageSettings getUserWelcome() {
        return userWelcome;
    }

    public void setUserWelcome(MessageSettings userWelcome) {
        this.userWelcome = userWelcome;
    }

    public MessageSettings getBotWelcome() {
        return botWelcome;
    }

    public void setBotWelcome(MessageSettings botWelcome) {
        this.botWelcome = botWelcome;
    }

    public MessageSettings getUserFarewell() {
        return userFarewell;
    }

    public void setUserFarewell(MessageSettings userFarewell) {
        this.userFarewell = userFarewell;
    }

    public MessageSettings getBotFarewell() {
        return botFarewell;
    }

    public void setBotFarewell(MessageSettings botFarewell) {
        this.botFarewell = botFarewell;
    }
}
