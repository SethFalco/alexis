package com.elypia.alexis.entities;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

/**
 * This is the data on the <strong>global</strong> instance of a user
 * and is in no way guild specific.
 */

@Entity(value = "users", noClassnameStored = true)
public class UserData {

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

    /**
     * The last time this user sent a message on Discord globally.
     * Used to determine if they earn XP or not.
     */

    @Property("last_active")
    private Date lastActive;

    /**
     * @param event The generic event which rewards XP.
     * @return The new amount of XP the user has.
     */

    public int gainXp(MessageReceivedEvent event) {
        int gains = isEligibleForXp(event);

        if (gains > 0)
            xp += gains;

        // Reset last message to current timestamp.
        lastActive.setTime(System.currentTimeMillis());
        return xp;
    }

    /**
     * Checks if the user is entitled to XP based on
     * the last time they sent a message. <br>
     * The return value with be -1 if it appears the user
     * has been typing faster than physically possible.
     * (10 characters in a second.)
     *
     * @param event The generic event which rewards XP.
     * @return Amount of XP user is entitled to, or -1 if deemed cheating.
     */

    public int isEligibleForXp(MessageReceivedEvent event) {
        // Split the message for every set of whitespace characters.
        String content = event.getMessage().getContentRaw();
        int length = content.split("\\s+").length;

        if (lastActive == null)
            return length;

        long current = System.currentTimeMillis();
        long previous = lastActive.getTime();

        // Divide by 1,000 to get milliseconds from seconds
        // Multiply by 10 for max chars allowed for the # of seconds passed.
        int allowableLength = (int)((current - previous) / 100);

        return length < allowableLength ? length : -1;
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

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }
}
