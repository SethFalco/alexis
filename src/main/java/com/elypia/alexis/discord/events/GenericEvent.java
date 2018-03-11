package com.elypia.alexis.discord.events;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.UserData;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.user.GenericUserEvent;

public class GenericEvent {

    private Chatbot chatbot;

    private GuildData guildData;
    private UserData userData;

    public GenericEvent(GenericGuildEvent event) {
        guildData = new GuildData(getDatabase("guilds"), event.getGuild());
    }

    public GenericEvent(GenericUserEvent event) {
        userData = new UserData(getDatabase("users"), event.getUser());
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
