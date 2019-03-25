package com.elypia.alexis.params;

import com.elypia.commandler.annotations.Param;
import com.elypia.jdac.JDACEvent;
import com.elypia.jdac.validation.Everyone;

public class BotSayParams {

    private JDACEvent event;

    @Everyone
    @Param("bot.param.body.say.help")
    private String body;

    public BotSayParams(JDACEvent event, String body) {
        this.event = event;
        this.body = body;
    }

    public JDACEvent getEvent() {
        return event;
    }

    public String getBody() {
        return body;
    }
}
