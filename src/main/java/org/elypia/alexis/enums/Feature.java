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

package org.elypia.alexis.enums;

public enum Feature {

    /**
     * Translate messages to the specified language when
     * country flag reactions are performed.
     */
    TRANSLATE_ON_REACTION(100),

    /**
     * Sync the name of the voice channel, or username of
     * the bot to the audio it's playing.
     */
    MUSIC_NAME_SYNC(101),

    /**
     * Display the global level instead of the current level
     * for the guild with this feature enabled.
     */
    DISPLAY_GLOBAL_LEVEL(102),

    /**
     * Track the roles in this guild, if a user leaves and joins
     * back, we'll give back all (with configurable exceptions)
     * roles they had before.
     */
    REAPPLY_PREVIOUS_ROLES(103),

    /** To send a message when a user (non-bot) joins the guild. */
    USER_JOIN_MESSAGES(200),

    /** To send a message when a user (non-bot) leaves the guild. */
    USER_LEAVE_MESSAGE(201),

    /** To send a message when a bot (non-user) joins the guild. */
    BOT_JOIN_MESSAGES(202),

    /** To send a message when a bot (non-user) leaves the guild. */
    BOT_LEAVE_MESSAGE(203),

    /** The role to grant to a user as soon as they join. */
    USER_JOIN_ROLE(300),

    /** The role to grant to a bot as soon as they join. */
    BOT_JOIN_ROLE(301),

    /** Send notifications in a channel when a user has leveled up globally. */
    GLOBAL_LEVEL_NOTIFICATION(400),

    /** Send a notification in a channel when a user has leveled up locally. */
    GUILD_LEVEL_NOTIFICATION(401);

    /**
     * The serialized identifier of the event. These should be as
     * persistent as possible for cross-version compatability.
     */
    private final int ID;

    Feature(final int ID) {
        this.ID = ID;
    }

    public int getId() {
        return ID;
    }
}
