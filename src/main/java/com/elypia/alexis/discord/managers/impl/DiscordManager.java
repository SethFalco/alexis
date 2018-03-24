package com.elypia.alexis.discord.managers.impl;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.ChatbotConfig;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class DiscordManager extends ListenerAdapter {
    /**
     * Parent chat bot instance this dispatcher is dispatching
     * message and reaction events too.
     */

    protected final Chatbot chatbot;

    /**
     * Configuration, contains all values loaded at start up
     * of the application.
     */

    protected final ChatbotConfig config;

    /**
     * @param chatbot Chatbot instance to forward message and
     *                reaction events too.
     */

    protected DiscordManager(final Chatbot chatbot) {
        this.chatbot = chatbot;
        this.config = chatbot.getConfig();
    }
}
