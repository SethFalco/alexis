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

package org.elypia.alexis.repositories;

import org.apache.deltaspike.data.api.*;
import org.elypia.alexis.entities.Skill;

import java.util.List;

@Repository(forEntity = Skill.class)
public interface SkillRepository extends EntityRepository<Skill, Integer> {

    List<Skill> findByGuildId(long guildId);

    int countByGuildIdAndNameEqualIgnoreCase(long guildId, String name);

    Skill findByGuildIdAndNameEqualIgnoreCase(long guildId, String name);

    int deleteByGuildIdAndNameEqualIgnoreCase(long guildId, String name);

    /**
     * Delete all skills associated with a guild.
     *
     * @param guildId The guild to delete skills from.
     * @return The number of rows that have been deleted.
     */
    int deleteByGuildId(long guildId);
}
