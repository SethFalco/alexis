package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.UserData;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.GenericUserEvent;

public class GenericEvent {

    private Chatbot chatbot;

    private Message message;

    private GuildData guildData;
    private UserData userData;

    public GenericEvent(GenericGuildEvent event) {
        guildData = new GuildData(getDatabase("guilds"), event.getGuild());
    }

    public GenericEvent(GenericUserEvent event) {
        userData = new UserData(getDatabase("users"), event.getUser());
    }

    public GenericEvent(MessageReceivedEvent event) {
        this.message = message;

        if (event.isFromType(ChannelType.TEXT))
            guildData = new GuildData(getDatabase("guilds"), event.getGuild());

        userData = new UserData(getDatabase("users"), event.getAuthor());
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
