package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.impl.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.types.ObjectId;
import xyz.morphia.annotations.*;

import java.util.Date;

/**
 * This is the data on the <strong>global</strong> instance of a user
 * and is in no way guild specific.
 */
@Entity(value = "users", noClassnameStored = true)
public class UserData extends Experienceable implements DatabaseEntity {

    @Id
    private ObjectId id;

    /**
     * The ID of the user on Discord and as stored in the database.
     */
    @Property("user_id")
    private long userId;

    @Property("last_message")
    private Date lastMessage;

    public UserData() {

    }

    public UserData(long userId) {
        this.userId = userId;
    }

    public static UserData query(long userId) {
        UserData data = query("user_id", userId);

        if (data == null)
            return new UserData(userId);

        return data;
    }

    public static <T> UserData query(String field, T value) {
        return Alexis.getDatabaseManager().query(UserData.class, field, value);
    }

    public boolean isEligibleForXp(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        int length = content.split("\\s+").length;
        long current = System.currentTimeMillis();

        if (lastMessage == null)
            return true;

        // ? Maximum of 1 character per 100 milliseconds.
        return length < ((current - lastMessage.getTime()) / 100);
    }


    /**
     * Calculate how much XP this entity is entitled to getting based on
     * the message received.
     *
     * @param event The message event to reward for.
     * @return The amount of XP the user is entitled to.
     */
    public int calculateXp(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        return content.split("\\s+").length;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }
}
