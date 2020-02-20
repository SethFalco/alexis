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
import org.elypia.alexis.entities.GuildData;

import java.util.Locale;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Repository(forEntity = GuildData.class)
public interface GuildRepository extends EntityRepository<GuildData, Long> {

    @Modifying
    @Query("UPDATE GuildData AS g SET g.locale = ?1 WHERE g.id = ?2")
    int updateLocale(final Locale locale, final long id);

    /**
     * @param id The ID of the guild.
     * @param prefix What to set he guild's prefix to.
     * @return The number of rows that changed, this should always be 1.
     */
    @Modifying
    @Query("UPDATE GuildData AS g SET g.prefix = ?1 WHERE g.id = ?2")
    int updatePrefix(final String prefix, final long id);

    @Modifying
    @Query("UPDATE GuildData AS g SET g.reactTranslation = ?1 WHERE g.id = ?2")
    int updateReactTranslation(final boolean toggle, final long id);
}
