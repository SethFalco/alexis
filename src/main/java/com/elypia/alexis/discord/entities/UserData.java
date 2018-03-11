package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Achievement;
import com.elypia.alexis.discord.entities.impl.DatabaseEntity;
import com.elypia.alexis.discord.events.GenericEvent;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class UserData extends DatabaseEntity {

    /**
     * The ID of the user on Discord and as stored in the database.
     */

    private long userId;

    /**
     * The total XP this user has.
     */

    private int xp;

    /**
     * All achievments this user has obtained. <br>
     * Do note: Achievments can be revoked and are primarily
     * for internal use.
     */

    private Collection<Achievement> achievements;

    /**
     * The last time this user sent a message on Discord globally.
     * Used to determine if they earn XP or not.
     */

    private Date lastMessage;

    /**
     * Query the database for all data on the user, and wrap this class
     * around it. If no user is found, then all values are default and when
     * {@link #commit()} is called the user will be inserted.
     *
     * @param database The database which stores chatbot data.
     * @param user The discord user we wish to get data for.
     */

    public UserData(MongoDatabase database, User user) {
        super(database, "users");
        userId = user.getIdLong();
        achievements = new ArrayList<>();

        Document data = getById("user_id", userId);

        if (data == null)
            return;

        xp = data.getInteger("xp");
        lastMessage = data.getDate("last_message");

        getArray(data, "achievements", o -> {
            Achievement a = Achievement.getByName((String)o);
            achievements.add(a);
        });
    }

    /**
     * Should only be called once we're finished with this object.
     * Commit all values in this value object to the database. If
     * this entry doesn't exist yet then create it.
     */

    @Override
    public void commit() {
        Document userData = new Document();
        userData.put("xp", xp);
        userData.put("achievements", achievements);

        collection.updateOne(eq("user_id", userId), userData, options);
    }

    /**
     * @param event The generic event which rewards XP.
     * @return The new amount of XP the user has.
     */

    public int gainXp(GenericEvent event) {
        int gains = isEligibleForXp(event);

        if (gains > 0)
            xp += gains;

        // Reset last message to current timestamp.
        lastMessage = new Date();
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

    public int isEligibleForXp(GenericEvent event) {
        long current = System.currentTimeMillis();
        long previous = lastMessage.getTime();

        // Split the message for every set of whitespace characters.
        int length = event.getContent().split("\\s+").length;

        // Divide by 1,000 to get milliseconds from seconds
        // Multiply by 10 for max chars allowed for the # of seconds passed.
        int allowableLength = (int)((current - previous) / 100);

        return length < allowableLength ? length : -1;
    }

    /**
     * @return Get the ID of the user.
     */

    public long getId() {
        return userId;
    }

    /**
     * @return Get the current XP of the user.
     */

    public int getXp() {
        return xp;
    }

    /**
     * @return The last time the user sent a message which was entitled to XP.
     */

    public Date getLastMessageDate() {
        return lastMessage;
    }

    /**
     * @param achievement The achievement to award to the user.
     */

    public void awardAchievement(Achievement achievement) {
        Objects.requireNonNull(achievement);
        achievements.add(achievement);
    }

    /**
     * Users can lose acheivements for reverting what they did to achieve it.
     * For example unauthenticating to an account.
     *
     * @param achievement The achievment to remove from the user.
     */

    public void revokeAchievement(Achievement achievement) {
        Objects.requireNonNull(achievement);
        achievements.remove(achievement);
    }

    /**
     * @return Any achievments the user has.
     */

    public Collection<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * @param achievement The achiemement we want.
     * @return If the user has this achievment.
     */

    public boolean hasAchievement(Achievement achievement) {
        return achievements.contains(achievement);
    }
}
