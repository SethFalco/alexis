package com.elypia.alexis.discord.handlers;

import com.elypia.alexis.discord.annotation.MessageEvent;
import com.elypia.alexis.discord.entities.GuildData;
import com.elypia.alexis.discord.entities.TagData;
import com.elypia.alexis.discord.entities.TextChannelData;
import com.elypia.alexis.discord.entities.UserData;
import com.elypia.alexis.discord.entities.data.Tag;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.Document;

/**
 * There are events that non-command messages have to
 * go through and may consist of misc tasks such as
 * nudity detection, use of banned words, granting XP
 * and so on. Actual commands will come here also, before
 * we bother to processing the actual intent.
 */

public class GlobalMessageHandler {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> guilds;
    private MongoCollection<Document> users;
    private MongoCollection<Document> members;

    public GlobalMessageHandler(MongoClient client) {
        database = client.getDatabase("alexis");

        guilds = database.getCollection("guilds");
        users = database.getCollection("users");
        members = database.getCollection("members");
    }

    @MessageEvent (
        requiresDatabase = true,
        scope = ChannelType.TEXT
    )
    public void handleXp(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        User user = event.getAuthor();
        TextChannel channel = event.getTextChannel();

        String content = event.getMessage().getContentRaw();
        int messageXp = content.split("\\s+").length;

        GuildData guildData = new GuildData(database, guild);
        UserData userData = new UserData(database, user);
        TextChannelData textChannelData = guildData.getTextChannelData(channel);

        TagData spam = textChannelData.getGuildData().isChannel(Tag.SPAM);
        if (spam != null) {
            if (spam.isEnabled()) {

            }
        }
    }
}
