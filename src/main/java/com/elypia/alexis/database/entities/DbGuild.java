package com.elypia.alexis.database.entities;

import javax.persistence.*;

/** The data representing a Discord Guild. */
@Entity(name = "guild")
@Table
public class DbGuild {

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

    /** If a role should be applied to users on join. */
    @Column(name = "join_role_user_enabled")
    private boolean joinRoleUserBoolean;

    /** What role to apply to users on join. */
    @Column(name = "join_role_user_id")
    private long joinRoleUserId;

    /** If a role should be applied to bots on join. */
    @Column(name = "join_role_bot_enabled")
    private boolean joinRoleBotBoolean;

    /** The role that should be applied to bots on join. */
    @Column(name = "join_role_bot_id")
    private long joinRoleBotId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isJoinRoleUserBoolean() {
        return joinRoleUserBoolean;
    }

    public void setJoinRoleUserBoolean(boolean joinRoleUserBoolean) {
        this.joinRoleUserBoolean = joinRoleUserBoolean;
    }

    public long getJoinRoleUserId() {
        return joinRoleUserId;
    }

    public void setJoinRoleUserId(long joinRoleUserId) {
        this.joinRoleUserId = joinRoleUserId;
    }

    public boolean isJoinRoleBotBoolean() {
        return joinRoleBotBoolean;
    }

    public void setJoinRoleBotBoolean(boolean joinRoleBotBoolean) {
        this.joinRoleBotBoolean = joinRoleBotBoolean;
    }

    public long getJoinRoleBotId() {
        return joinRoleBotId;
    }

    public void setJoinRoleBotId(long joinRoleBotId) {
        this.joinRoleBotId = joinRoleBotId;
    }
}
