package com.elypia.alexis.discord.managers.impl;

import com.elypia.alexis.discord.Chatbot;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class DiscordManager extends ListenerAdapter {
    /**
     * Parent chat bot instance this dispatcher is dispatching
     * message and reaction events too.
     */

    protected final Chatbot chatbot;

    /**
     * @param chatbot Chatbot instance to forward message and
     *                reaction events too.
     */

    protected DiscordManager(final Chatbot chatbot) {
        this.chatbot = chatbot;
    }
}
