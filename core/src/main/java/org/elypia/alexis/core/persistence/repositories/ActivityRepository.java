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

package org.elypia.alexis.core.persistence.repositories;

import org.apache.deltaspike.data.api.*;
import org.elypia.alexis.core.persistence.entities.ActivityData;

import java.util.Optional;

@Repository(forEntity = ActivityData.class)
public interface ActivityRepository extends EntityRepository<ActivityData, Integer> {

    /**
     * @return The activity data of any row that's enabled.
     */
    Optional<ActivityData> findAnyByEnabledTrue();

    /**
     * @param activityId An activity ID to ignore to ensure it's never returned.
     * @return The activity data of any row that's enabled and not ignored.
     */
    Optional<ActivityData> findAnyByEnabledTrueAndIdGreaterThan(int activityId);
}
