package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.embedded.MemberSkill;
import com.elypia.alexis.entities.impl.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;

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

    @Property("guild_id")
    private long guildId;

    @Property("last_message")
    private Date lastMessage;

    @Embedded("skills")
    private List<MemberSkill> skills;

    public MemberData() {

    }

    public MemberData(long userId, long guildId) {
        this.userId = userId;
        this.guildId = guildId;
    }

    public static MemberData query(long userId, long guildId) {
        Map<String, Object> params = Map.of("user_id", userId, "guild_id", guildId);
        var data = Alexis.getDatabaseManager().query(MemberData.class, params);

        if (data == null)
            data = new MemberData(userId, guildId);

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

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
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
