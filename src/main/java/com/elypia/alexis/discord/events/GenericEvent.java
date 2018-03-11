package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.UserData;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.GenericUserEvent;

/**
 * This is extremely generic, in runtime maybe fields may be
 * null depending on when, where or what event actually
 * instantiated the object.
 */

public class GenericEvent {

    /**
     * JDA / bot client.
     */

    protected JDA jda;

    /**
     * Wrapper around {@link JDA} with additional configuration
     * and databases.
     */

    protected Chatbot chatbot;

    /**
     * Generic event with very limited information but can be
     * cast we're aware what instance it is at the time.
     */

    protected Event event;

    /**
     * Only non-null for messages regardless of where they came from
     * and will <strong>not</strong> have a value anywhere else.
     */

    protected Message message;

    /**
     * The message channel the action was executed, this could
     * be a text channel if in a guild or a private channel if in PM.
     */

    protected MessageChannel channel;

    /**
     * Only non-null for events that inherit from {@link GenericGuildEvent}.
     */

    protected GuildData guildData;

    /**
     * Only non-null for events which target or are executed by a
     * particular Discord user.
     */

    protected UserData userData;

    public GenericEvent(Chatbot chatbot, Event genericEvent) {
        this.chatbot = chatbot;
        this.event = genericEvent;
        jda = genericEvent.getJDA();
    }

    public GenericEvent(Chatbot chatbot, GenericGuildEvent event) {
        this(chatbot, (Event)event);
        guildData = new GuildData(chatbot.getHomeDatabase(), event.getGuild());
    }

    public GenericEvent(Chatbot chatbot, GenericUserEvent event) {
        this(chatbot, (Event)event);
        userData = new UserData(chatbot.getHomeDatabase(), event.getUser());
    }

    public GenericEvent(Chatbot chatbot, MessageReceivedEvent event) {
        this(chatbot, (Event)event);
        this.message = event.getMessage();

        if (event.isFromType(ChannelType.TEXT))
            guildData = new GuildData(chatbot.getHomeDatabase(), event.getGuild());

        userData = new UserData(chatbot.getHomeDatabase(), event.getAuthor());
        channel = event.getChannel();
    }

    public Chatbot getChatbot() {
        return chatbot;
    }

    public Message getMessage() {
        return message;
    }

    public String getContent() {
        return message.getContentRaw();
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public MongoDatabase getDatabase(String database) {
        return chatbot.getDatabase(database);
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public UserData getUserData() {
        return userData;
    }
}
