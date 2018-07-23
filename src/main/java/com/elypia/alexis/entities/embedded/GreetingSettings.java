package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GreetingSettings {

    @Embedded("welcome")
    private GreetingSetting welcome;

    @Embedded("farewell")
    private GreetingSetting farewell;

    public GreetingSetting getWelcome() {
        if (welcome == null)
            welcome = new GreetingSetting();

        return welcome;
    }

    public void setWelcome(GreetingSetting welcome) {
        this.welcome = welcome;
    }

    public GreetingSetting getFarewell() {
        if (farewell == null)
            farewell = new GreetingSetting();

        return farewell;
    }

    public void setFarewell(GreetingSetting farewell) {
        this.farewell = farewell;
    }
}
