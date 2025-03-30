/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.core.persistence.entities;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fun.falco.alexis.core.persistence.enums.Feature;
import fun.falco.alexis.core.persistence.enums.GuildMessageType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The data representing a Discord Guild.
 *
 * @author seth@falco.fun (Seth Falco)
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

    @ColumnDefault("'en_US'")
    @Column(name = "guild_locale", nullable = false)
    private Locale locale;

    /** The prefix that must be before a command, or null if this guild only allows mentions. */
    @Column(name = "guild_prefix")
    private String prefix;

    /**
     * We'll delete all data we have on a guild if they kick the bot by default.
     * If a user wants us to hold onto data even if they kick us, they can
     * set how long we should hold onto it for.
     */
    @Column(name = "data_retention_duration")
    private Duration dataRetentionDuration;

    /** The features that have been manually configured in this guild. */
    @MapKey(name = "feature")
    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(targetEntity = FeatureSettings.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Feature, FeatureSettings> features;

    @MapKey(name = "type")
    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(targetEntity = GuildMessage.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<GuildMessageType, GuildMessage> messages;

    @OneToMany(targetEntity = CustomCommand.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomCommand> customCommands;

    @OneToMany(targetEntity = EmoteData.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmoteData> emotes;

    @OneToMany(targetEntity = EmoteUsage.class, mappedBy = "usageGuildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmoteUsage> emoteUsages;

    @OneToMany(targetEntity = MessageChannelData.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageChannelData> messageChannels;

    @OneToMany(targetEntity = RoleData.class, mappedBy = "guildData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleData> roles;

    public GuildData() {
        features = new EnumMap<>(Feature.class);
        messages = new EnumMap<>(GuildMessageType.class);
        customCommands = new ArrayList<>();
        emotes = new ArrayList<>();
        emoteUsages = new ArrayList<>();
        messageChannels = new ArrayList<>();
        roles = new ArrayList<>();
    }

    public GuildData(final long id) {
        this();
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GuildData)) {
            return false;
        }

        GuildData g = (GuildData) o;

        return id == g.id &&
            description.equals(g.description) &&
            locale.equals(g.locale) &&
            prefix.equals(g.prefix);
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

    public Duration getDataRetentionDuration() {
        return dataRetentionDuration;
    }

    public void setDataRetentionDuration(Duration dataRetentionDuration) {
        this.dataRetentionDuration = dataRetentionDuration;
    }

    public Map<Feature, FeatureSettings> getFeatures() {
        return features;
    }

    public void setFeatures(Map<Feature, FeatureSettings> features) {
        this.features = features;
    }

    public void addFeature(FeatureSettings feature) {
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
}
