package com.elypia.alexis.params;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Param;
import com.elypia.disco.constraints.Everyone;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public class BotSayParams {

    private CommandlerEvent<GenericMessageEvent> event;

    @Everyone
    @Param("bot.param.body.say.help")
    private String body;

    public BotSayParams(CommandlerEvent<GenericMessageEvent> event, String body) {
        this.event = event;
        this.body = body;
    }

    public CommandlerEvent<GenericMessageEvent> getEvent() {
        return event;
    }

    public String getBody() {
        return body;
    }
}
