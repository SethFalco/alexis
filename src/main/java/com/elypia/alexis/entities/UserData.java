package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.data.Achievement;
import com.elypia.alexis.entities.embedded.NanowrimoLink;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;

import java.util.*;

/**
 * This is the data on the <strong>global</strong> instance of a user
 * and is in no way guild specific.
 */
@Entity(value = "users", noClassnameStored = true)
public class UserData extends DatabaseEntity {

    @Id
    private ObjectId id;

    /**
     * The ID of the user on Discord and as stored in the database.
     */
    @Property("user_id")
    private long userId;

    /**
     * The total XP this user has.
     */
    @Property("xp")
    private int xp;

    @Property("achivements")
    private Set<Achievement> achievements;

    @Embedded("nanowrimo")
    private NanowrimoLink nanoLink;

    @Property("last_message")
    private Date lastMessage;

    public static UserData query(long id) {
        Datastore store = Alexis.store;
        Query<UserData> query = store.createQuery(UserData.class);
        UserData data = query.filter("user_id ==", id).get();

        if (data == null) {
            data = new UserData();
            data.userId = id;
        }

        if (data.achievements == null)
            data.achievements = new HashSet<>();

        return data;
    }

    public static UserData query(String field, String value) {
        Datastore store = Alexis.store;
        Query<UserData> query = store.createQuery(UserData.class);
        return query.filter(field + " ==", value).get();
    }

    public void grantAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public void revokeAchievement(Achievement achievement) {
        achievements.remove(achievement);
    }

    /**
     * Checks if the user is entitled to XP based on
     * the last time they sent a message. <br>
     * The return value with be -1 if it appears the user
     * has been typing faster than physically possible.
     * (10 characters in a second.)
     *
     * @param event The generic event which rewards XP.
     * @return If they were entitled to xp.
     */
    public boolean grantXp(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        int length = content.split("\\s+").length;
        long current = System.currentTimeMillis();
        boolean earned = lastMessage == null || length <= ((current - lastMessage.getTime()) / 100);

        lastMessage = new Date();

        if (earned)
            xp += length;

        return earned;
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

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievments) {
        this.achievements = achievments;
    }

    public NanowrimoLink getNanoLink() {
        if (nanoLink == null)
            nanoLink = new NanowrimoLink();

        return nanoLink;
    }

    public void setNanoLink(NanowrimoLink nanoLink) {
        this.nanoLink = nanoLink;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }
}
