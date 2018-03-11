package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.impl.DatabaseEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class UserData implements DatabaseEntity {

    private MongoDatabase database;
    private MongoCollection<Document> users;

    private long userId;
    private int xp;

    public UserData(MongoDatabase database, User user) {
        this.database = database;
        users = database.getCollection("users");

        userId = user.getIdLong();

        Document userData = users.find(eq("user_id", userId)).first();

        if (userData != null)
            xp = userData.getInteger("xp");
    }

    public void commit() {
        Document userData = new Document();
        userData.put("user_id", userId);
        userData.put("xp", xp);

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);

        users.updateOne(eq("user_id", userId), userData, options);
    }
}
