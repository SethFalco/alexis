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

package fun.falco.alexis.core.persistence.repositories;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import fun.falco.alexis.core.persistence.entities.GuildMessage;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Repository(forEntity = GuildMessage.class)
public interface GuildMessageRepository extends EntityRepository<GuildMessage, Long> {

    @Query("SELECT GuildMessage AS gm WHERE guildData.id = ?1 AND type = ?2")
    GuildMessage findByGuildIdAndType(long guildId, int type);
}
