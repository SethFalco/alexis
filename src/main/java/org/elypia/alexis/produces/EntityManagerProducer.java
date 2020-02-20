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

package org.elypia.alexis.produces;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.*;
import javax.persistence.*;
import java.io.Closeable;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
@Singleton
public class EntityManagerProducer implements Closeable {

    private final EntityManagerFactory factory;

    @Inject
    public EntityManagerProducer(@PersistenceUnitName("") EntityManagerFactory factory) {
        this.factory = factory;
    }

    @ApplicationScoped
    @Produces
    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em != null && em.isOpen())
            em.close();
    }

    @PreDestroy
    @Override
    public void close() {
        if (factory.isOpen())
            factory.close();
    }
}
