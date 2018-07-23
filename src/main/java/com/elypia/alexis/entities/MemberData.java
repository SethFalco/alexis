package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;

import java.util.Date;

@Entity(value = "members", noClassnameStored = true)
public class MemberData extends DatabaseEntity {

    @Id
    private ObjectId id;

    /**
     * The ID of the user on Discord and as stored in the database.
     */

    @Property("user_id")
    private long userId;

    @Property("guild_id")
    private long guildId;

    /**
     * The total XP this user has.
     */

    @Property("xp")
    private int xp;

    @Property("last_message")
    private Date lastMessage;

    public static MemberData query(long userId, long guildId) {
        Datastore store = Alexis.getChatbot().getDatastore();
        Query<MemberData> query = store.createQuery(MemberData.class);
        MemberData data = query.filter("user_id ==", userId).filter("guild_id ==", guildId).get();

        if (data == null) {
            data = new MemberData();
            data.userId = userId;
            data.guildId = guildId;
        }

        return data;
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
// ! This is copied code, we need to make it reusable between anything that can gain xp. (users, members, guilds)
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

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }
}
