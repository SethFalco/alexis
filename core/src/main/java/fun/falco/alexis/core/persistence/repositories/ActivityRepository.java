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

import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import fun.falco.alexis.core.persistence.entities.ActivityData;

@Repository(forEntity = ActivityData.class)
public interface ActivityRepository extends EntityRepository<ActivityData, Integer> {

    /**
     * @return Activity data of any row that's enabled.
     */
    Optional<ActivityData> findAnyByEnabledTrue();

    /**
     * @param activityId Activity ID to ignore to ensure it's never returned.
     * @return Activity data of any row that's enabled and not ignored.
     */
    Optional<ActivityData> findAnyByEnabledTrueAndIdGreaterThan(int activityId);
}
