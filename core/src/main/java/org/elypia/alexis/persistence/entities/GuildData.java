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

package org.elypia.alexis.persistence.entities;

import org.elypia.alexis.persistence.enums.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

/**
 * The data representing a Discord Guild.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "guild")
public class GuildData implements Serializable {

    private static final long serialVersionUID = 1;

    /** The ID of the Guild. */
    @Id
    @Column(name = "guild_id")
    private long id;

    @Column(name = "description")
    private String description;

    /** The total XP earned in this Guild. */
    @ColumnDefault("0")
    @Column(name = "guild_xp", nullable = false)
    private long xp;

    @ColumnDefault("'en_US'")
    @Column(name = "guild_locale", nullable = false)
    private Locale locale;

    /** The prefix that must be before a command, or null if this guild only allows mentions. */
    @Column(name = "guild_prefix")
    private String prefix;

    /** What role to apply to users on join. */
    @Column(name = "join_role_user_id", unique = true)
    private Long joinRoleUserId;

    /** The role that should be applied to bots on join. */
    @Column(name = "join_role_bot_id", unique = true)
    private Long joinRoleBotId;

    /**
     * Allow guilds to locally override the amount of XP awarded with a custom
     * multiplier.
     */
    @ColumnDefault("1.0")
    @Column(name = "xp_multp", nullable = false)
    private Double multiplier;

    /** The features that have been manually configured in this guild. */
    @MapKey(name = "feature")
    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(targetEntity = GuildFeature.class, mappedBy = "guildData")
    private Map<Feature, GuildFeature> features;

    @MapKey(name = "type")
    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(targetEntity = GuildMessage.class, mappedBy = "guildData")
    private Map<GuildMessageType, GuildMessage> messages;

    @MapKey(name = "logType")
    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(targetEntity = LogSubscription.class, mappedBy = "guildData")
    private Map<LogType, LogSubscription> logSubscriptions;

    @OneToMany(targetEntity = CustomCommand.class, mappedBy = "guildData")
    private List<CustomCommand> customCommands;

    @OneToMany(targetEntity = EmoteData.class, mappedBy = "guildData")
    private List<EmoteData> emotes;

    @OneToMany(targetEntity = EmoteUsage.class, mappedBy = "guildData")
    private List<EmoteUsage> emoteUsages;

    @OneToMany(targetEntity = MemberData.class, mappedBy = "guildData")
    private List<MemberData> members;

    @OneToMany(targetEntity = MessageChannelData.class, mappedBy = "guildData")
    private List<MessageChannelData> messageChannels;

    @OneToMany(targetEntity = RoleData.class, mappedBy = "guildData")
    private List<RoleData> roles;

    @OneToMany(targetEntity = Skill.class, mappedBy = "guild", cascade = CascadeType.ALL)
    private List<Skill> skills;

    public GuildData() {
        // Do nothing
    }

    public GuildData(final long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GuildData))
            return false;

        GuildData g = (GuildData)o;

        return id == g.id &&
            description.equals(g.description) &&
            xp == g.xp &&
            locale.equals(g.locale) &&
            prefix.equals(g.prefix) &&
            joinRoleUserId.equals(g.joinRoleUserId) &&
            joinRoleBotId.equals(g.joinRoleBotId) &&
            multiplier.equals(g.multiplier);
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double mutlipler) {
        this.multiplier = mutlipler;
    }

    public Map<Feature, GuildFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Map<Feature, GuildFeature> features) {
        this.features = features;
    }

    public void addFeature(GuildFeature feature) {
        features.put(feature.getFeature(), feature);
    }

    public Map<GuildMessageType, GuildMessage> getMessages() {
        return messages;
    }

    public void setMessages(Map<GuildMessageType, GuildMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(GuildMessage message) {
        messages.put(message.getType(), message);
    }

    public Map<LogType, LogSubscription> getLogSubscriptions() {
        return logSubscriptions;
    }

    public void setLogSubscriptions(Map<LogType, LogSubscription> logSubscriptions) {
        this.logSubscriptions = logSubscriptions;
    }

    public void addLogSubscriptions(LogSubscription subscription) {
        logSubscriptions.put(subscription.getLogType(), subscription);
    }

    public List<CustomCommand> getCustomCommands() {
        return customCommands;
    }

    public void setCustomCommands(List<CustomCommand> customCommands) {
        this.customCommands = customCommands;
    }

    public List<EmoteData> getEmotes() {
        return emotes;
    }

    public void setEmotes(List<EmoteData> emotes) {
        this.emotes = emotes;
    }

    public List<EmoteUsage> getEmoteUsages() {
        return emoteUsages;
    }

    public void setEmoteUsages(List<EmoteUsage> emoteUsages) {
        this.emoteUsages = emoteUsages;
    }

    public List<MemberData> getMembers() {
        return members;
    }

    public void setMembers(List<MemberData> members) {
        this.members = members;
    }

    public List<MessageChannelData> getMessageChannels() {
        return messageChannels;
    }

    public void setMessageChannels(List<MessageChannelData> messageChannelData) {
        this.messageChannels = messageChannelData;
    }

    public List<RoleData> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleData> roles) {
        this.roles = roles;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
