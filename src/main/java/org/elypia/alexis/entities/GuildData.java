/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.entities;

import javax.persistence.*;
import java.util.Locale;

/**
 * The data representing a Discord Guild.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Entity(name = "guild")
@Table
public class GuildData {

    /** The ID of the Guild. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guild_id")
    private long id;

    /** The total XP earned in this Guild. */
    @Column(name = "guild_xp")
    private long xp;

    @Column(name = "guild_locale")
    private Locale locale;

    /** The prefix that must be before a command, or null if this guild only allows mentions. */
    @Column(name = "guild_prefix")
    private String prefix;

    @Column(name = "react_translate")
    private boolean reactTranslation;

    /** What role to apply to users on join. */
    @Column(name = "join_role_user_id")
    private Long joinRoleUserId;

    /** The role that should be applied to bots on join. */
    @Column(name = "join_role_bot_id")
    private Long joinRoleBotId;

    @Column(name = "xp_multp")
    private float mutlipler;

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

    public float getMutlipler() {
        return mutlipler;
    }

    public void setMutlipler(float mutlipler) {
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
