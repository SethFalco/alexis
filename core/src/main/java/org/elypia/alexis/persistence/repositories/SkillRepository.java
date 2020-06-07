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

package org.elypia.alexis.persistence.repositories;

import org.apache.deltaspike.data.api.*;
import org.elypia.alexis.persistence.entities.Skill;

import java.util.*;

@Repository(forEntity = Skill.class)
public interface SkillRepository extends EntityRepository<Skill, Integer> {

    /**
     * Check if a skill exists in this guild with the specified name.
     *
     * @param guildId The guild to check for.
     * @param name The name of the skill to check for.
     * @return The number of skills found, should be 1 or 0.
     */
    @Query("SELECT COUNT(s) FROM Skill s WHERE guild.id = ?1 AND name = ?2")
    int countByGuildIdAndNameEqualIgnoreCase(long guildId, String name);

    @Query("SELECT s FROM Skill s WHERE guild.id = ?1")
    List<Skill> findByGuild(long guildId);

    @Query("SELECT s FROM Skill s WHERE guild.id = ?1 AND name = ?2")
    Optional<Skill> findByGuildAndNameEqualIgnoreCase(long guildId, String name);
}
