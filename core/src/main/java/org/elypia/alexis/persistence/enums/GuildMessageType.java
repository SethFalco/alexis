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
 * A map for storing unique types of message data in
 * the database for custom messages.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public enum GuildMessageType {

    /** When a user joins a guild. */
    USER_WELCOME("user welcome"),

    /** When a bot joins a guild. */
    BOT_WELCOME("bot welcome"),

    /** When a user leaves a guild. */
    USER_LEAVE("user farewell"),

    /** When a bot leaves a guild. */
    BOT_LEAVE("bot farewell"),

    /** When a user levels up globally. */
    USER_LEVEL_UP("user level up"),

    /** When a member levels up locally in a server. */
    MEMBER_LEVEL_UP("member level up"),

    /** When a guild levels up. */
    GUILD_LEVEL_UP("guild level up");

    /** A more user friendly name that can be used in messages. */
    private final String FRIENDLY_NAME;

    GuildMessageType(String friendlyName) {
        this.FRIENDLY_NAME = friendlyName;
    }

    public String getFriendlyName() {
        return FRIENDLY_NAME;
    }
}
