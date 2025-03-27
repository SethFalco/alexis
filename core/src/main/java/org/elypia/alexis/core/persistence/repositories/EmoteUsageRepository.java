package org.elypia.alexis.core.persistence.repositories;

import org.apache.deltaspike.data.api.*;
import org.elypia.alexis.core.persistence.entities.EmoteUsage;

import java.util.List;

@Repository(forEntity = EmoteUsage.class)
public interface EmoteUsageRepository extends EntityRepository<EmoteUsage, Integer> {

    List<EmoteUsage> findByGuildId(long guildId);
}
