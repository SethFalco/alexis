package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class GreetingSettings {

    @Embedded("join")
    private GreetingSetting join;

    @Embedded("leave")
    private GreetingSetting leave;

    public GreetingSetting getJoin() {
        if (join == null)
            join = new GreetingSetting();

        return join;
    }

    public void setWelcome(GreetingSetting join) {
        this.join = join;
    }

    public GreetingSetting getLeave() {
        if (leave == null)
            leave = new GreetingSetting();

        return leave;
    }

    public void setFarewell(GreetingSetting leave) {
        this.leave = leave;
    }
}
