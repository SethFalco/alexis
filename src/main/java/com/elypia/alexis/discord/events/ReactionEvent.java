package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public class ReactionEvent extends GenericEvent {

    public ReactionEvent(Chatbot chatbot, MessageReactionAddEvent event) {
        super(chatbot, event);
    }
}
