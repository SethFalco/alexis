/*
 * Copyright 2019-2020 Seth Falco and Alexis Contributors
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

package org.elypia.alexis.core.persistence.enums;

/**
 * A map of features that guilds can toggle on or off.
 * This data does not map to actual configurations of said features.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public enum Feature {

    /**
     * Should Alexis count up all emote usages in for emotes that
     * belong to this guild, that are used in this guild.
     */
    COUNT_GUILD_EMOTE_USAGE("count global emote usage"),

    /** To send a message when a user (non-bot) joins the guild. */
    USER_JOIN_MESSAGE("user welcome messages"),

    /** To send a message when a user (non-bot) leaves the guild. */
    USER_LEAVE_MESSAGE("user farewell messages"),

    /** To send a message when a bot (non-user) joins the guild. */
    BOT_JOIN_MESSAGE("bot welcome messages"),

    /** To send a message when a bot (non-user) leaves the guild. */
    BOT_LEAVE_MESSAGE("bot farewell messages");

    /** A more user friendly name that can be used in messages. */
    private final String friendlyName;

    Feature(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
