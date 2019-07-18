package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "member")
@Table
public class MemberData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int id;

    @Column(name = "guild_id")
    private long guildId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "member_xp")
    private long xp;

    public int getId() {
        return id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }
}
