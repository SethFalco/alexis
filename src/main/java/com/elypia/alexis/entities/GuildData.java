package com.elypia.alexis.entities;

import javax.persistence.*;
import java.util.List;

/** The data representing a Discord Guild. */
@Entity(name = "guild")
@Table
public class GuildData {

    /** The ID of the Guild. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guild_id")
    private int id;

    /** The total XP earned in this Guild. */
    @Column(name = "guild_xp")
    private long xp;

    /** The prefix that must be before a command, or null if this guild only allows mentions. */
    @Column(name = "guild_prefix")
    private String prefix;

    /** What role to apply to users on join. */
    @Column(name = "join_role_user_id")
    private long joinRoleUserId;

    /** The role that should be applied to bots on join. */
    @Column(name = "join_role_bot_id")
    private long joinRoleBotId;

    @Column(name = "multipler")
    private float mutlipler;

    /** Message settings such as the custom message and where to send it. */
    private List<MessageSetting> messages;

    /** The features that are enabled in this guild. */
    private List<GuildFeature> features;

    /** Log subscriptions for the bot managed audit channel. */
    private List<LogSubscription> subscriptions;

    // TODO: This is TEMP, we probably want a better way to do this.
    public GuildFeature getFeature(String id) {
        for (GuildFeature feature : features) {
            if (feature.getName().equalsIgnoreCase(id))
                return feature;
        }

        return null;
    }

    // TODO: This is TEMP, we probably want a better way to do this.
    public MessageSetting getMessage(int id) {
        for (MessageSetting message : messages) {
            if (message.getType() == id)
                return message;
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getJoinRoleUserId() {
        return joinRoleUserId;
    }

    public void setJoinRoleUserId(long joinRoleUserId) {
        this.joinRoleUserId = joinRoleUserId;
    }

    public long getJoinRoleBotId() {
        return joinRoleBotId;
    }

    public void setJoinRoleBotId(long joinRoleBotId) {
        this.joinRoleBotId = joinRoleBotId;
    }

    public float getMutlipler() {
        return mutlipler;
    }

    public void setMutlipler(float mutlipler) {
        this.mutlipler = mutlipler;
    }

    public List<LogSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<LogSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
