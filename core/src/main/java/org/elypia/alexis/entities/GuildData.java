/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.entities;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;

/**
 * The data representing a Discord Guild.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(name = "guild")
public class GuildData implements Serializable {

    private static final long serialVersionUID = 1;

    /** The ID of the Guild. */
    @Id
    @Column(name = "guild_id")
    private long id;

    /** The total XP earned in this Guild. */
    @ColumnDefault("0")
    @Column(name = "guild_xp", nullable = false)
    private long xp;

    @ColumnDefault("'en_US'")
    @Column(name = "guild_locale", nullable = false)
    private Locale locale;

    /** The prefix that must be before a command, or null if this guild only allows mentions. */
    @ColumnDefault("'$'")
    @Column(name = "guild_prefix")
    private String prefix;

    @ColumnDefault("0")
    @Column(name = "react_translate")
    private boolean reactTranslation;

    /** What role to apply to users on join. */
    @Column(name = "join_role_user_id", unique = true)
    private Long joinRoleUserId;

    /** The role that should be applied to bots on join. */
    @Column(name = "join_role_bot_id", unique = true)
    private Long joinRoleBotId;

    @ColumnDefault("1.0")
    @Column(name = "xp_multp", nullable = false)
    private double mutlipler;

    /** Message settings such as the custom message and where to send it. */
//    private List<GuildMessage> messages;

    /** The features that are enabled in this guild. */
//    @OneToMany(targetEntity = GuildFeature.class, mappedBy = "guildId", fetch = FetchType.EAGER)
//    private List<GuildFeature> features;

    /** Log subscriptions for the bot managed audit channel. */
//    private List<LogSubscription> subscriptions;

    public GuildData() {
        // Do nothing
    }

    public GuildData(final long id) {
        this.id = id;
    }

//    public GuildFeature getFeature(int id) {
//        for (GuildFeature feature : features) {
//            if (feature.getFeature() == id)
//                return feature;
//        }
//
//        return null;
//    }
//
//    public GuildMessage getMessage(int id) {
//        for (GuildMessage message : messages) {
//            if (message.getType() == id)
//                return message;
//        }
//
//        return null;
//    }

    public long getId() {
        return id;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isReactTranslation() {
        return reactTranslation;
    }

    public void setReactTranslation(boolean reactTranslation) {
        this.reactTranslation = reactTranslation;
    }

    public Long getJoinRoleUserId() {
        return joinRoleUserId;
    }

    public void setJoinRoleUserId(Long joinRoleUserId) {
        this.joinRoleUserId = joinRoleUserId;
    }

    public Long getJoinRoleBotId() {
        return joinRoleBotId;
    }

    public void setJoinRoleBotId(Long joinRoleBotId) {
        this.joinRoleBotId = joinRoleBotId;
    }

    public double getMutlipler() {
        return mutlipler;
    }

    public void setMutlipler(double mutlipler) {
        this.mutlipler = mutlipler;
    }

    //    public List<LogSubscription> getSubscriptions() {
//        return subscriptions;
//    }
//
//    public void setSubscriptions(List<LogSubscription> subscriptions) {
//        this.subscriptions = subscriptions;
//    }
}
