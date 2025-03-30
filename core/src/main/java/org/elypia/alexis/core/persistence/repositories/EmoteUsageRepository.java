package org.elypia.alexis.core.persistence.repositories;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.elypia.alexis.core.persistence.entities.EmoteUsage;

@Repository(forEntity = EmoteUsage.class)
public interface EmoteUsageRepository extends EntityRepository<EmoteUsage, Integer> {

    List<EmoteUsage> findByGuildId(long guildId);
}
