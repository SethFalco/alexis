package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.embedded.MemberSkill;
import com.elypia.alexis.entities.impl.*;
import org.bson.types.ObjectId;
import xyz.morphia.annotations.*;

import java.util.*;

@Entity(value = "members", noClassnameStored = true)
public class MemberData extends Experienceable implements DatabaseEntity {

    @Id
    private ObjectId id;

    /**
     * The ID of the user on Discord and as stored in the database.
     */
    @Property("user_id")
    private long userId;

    /**
     * The ID of the guild this member is in.
     */
    @Property("guild_id")
    private long guildId;

    @Embedded("skills")
    private List<MemberSkill> skills;

    public MemberData() {

    }

    public MemberData(long userId, long guildId) {
        this.userId = userId;
        this.guildId = guildId;
    }

    /**
     * Query the database for the data that represents the respective member.
     * If a member is not found, create a new instance with the
     * {@link #userId} and {@link #guildId} initialised.
     *
     * @param userId The Discord ID of the user.
     * @param guildId The Discord ID of the guild this use is in.
     * @return The data this bot stores for this object.
     */
    public static MemberData query(long userId, long guildId) {
        var params = Map.of(
            "user_id" , userId,
            "guild_id", guildId
        );

        MemberData data = Alexis.getDatabaseManager().query(MemberData.class, params);

        if (data == null)
            return new MemberData(userId, guildId);

        return data;
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

    public List<MemberSkill> getSkills() {
        if (skills == null)
            skills = new ArrayList<>();

        return skills;
    }

    public void setSkills(List<MemberSkill> skills) {
        this.skills = skills;
    }
}
