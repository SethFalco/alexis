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

package org.elypia.alexis.persistence.enums;

/**
 * A map of features that guilds can toggle on or off.
 * This data does not map to actual configurations of said features.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public enum Feature {

    /**
     * Translate messages to the specified language when
     * country flag reactions are performed.
     */
    TRANSLATE_ON_REACTION("translate on reaction"),

    /**
     * Sync the name of the voice channel, or username of
     * the bot to the audio it's playing.
     */
    MUSIC_NAME_SYNC("sync name with music"),

    /**
     * Display the global level instead of the current level
     * for the guild with this feature enabled.
     */
    DISPLAY_GLOBAL_LEVEL("display global levels"),

    /**
     * Track the roles in this guild, if a user leaves and joins
     * back, we'll give back all (with configurable exceptions)
     * roles they had before.
     */
    REAPPLY_PREVIOUS_ROLES("store and reapply roles"),

    /**
     * Should Alexis count up all emote usages in for emotes that
     * belong to this guild, that are used in this guild.
     */
    COUNT_GUILD_EMOTE_USAGE("count global emote usage"),

    /**
     * Should Alexis count up all emote usages for emotes that
     * belong to this guild, or are mutually visible between users
     * in the guild, and Alexis.
     */
    COUNT_MUTUAL_EMOTE_USAGE("count mutual emote usage"),

    /** To send a message when a user (non-bot) joins the guild. */
    USER_JOIN_MESSAGE("user welcome messages"),

    /** To send a message when a user (non-bot) leaves the guild. */
    USER_LEAVE_MESSAGE("user farewell messages"),

    /** To send a message when a bot (non-user) joins the guild. */
    BOT_JOIN_MESSAGE("bot welcome messages"),

    /** To send a message when a bot (non-user) leaves the guild. */
    BOT_LEAVE_MESSAGE("bot farewell messages"),

    /** To add a join role to users (non-bots) upon joining. */
    USER_JOIN_ROLE("user join role"),

    /** To add a join role to bots (non-users) upon joining. */
    BOT_JOIN_ROLE("bot join role"),

    /** Send notifications in a channel when a user has leveled up globally. */
    GLOBAL_LEVEL_NOTIFICATION("global level notifications"),

    /** Send a notification in a channel when a user has leveled up locally. */
    GUILD_LEVEL_NOTIFICATION("guild level notifications");

    /** A more user friendly name that can be used in messages. */
    private final String friendlyName;

    Feature(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
