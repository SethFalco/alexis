package com.elypia.alexis.entities;

import javax.persistence.*;

/**
 * Allows guilds to choose what messages they are subscribed too.
 */
@Entity(name = "log_subscription")
@Table
public class LogSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private int subscriptionId;

    @Column(name = "guild_id")
    private long guildId;

    @Column(name = "log_type_id")
    private int logTypeId;

    @Column(name = "subscribed")
    private boolean subscribed;

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(int logTypeId) {
        this.logTypeId = logTypeId;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
