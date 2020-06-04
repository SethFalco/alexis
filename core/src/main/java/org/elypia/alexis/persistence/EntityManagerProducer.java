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

package org.elypia.alexis.persistence;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;
import org.slf4j.*;

import javax.annotation.PreDestroy;
import javax.enterprise.context.*;
import javax.enterprise.inject.*;
import javax.inject.Inject;
import javax.persistence.*;
import java.io.Closeable;
import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
@ApplicationScoped
public class EntityManagerProducer implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(EntityManagerProducer.class);

    private final EntityManagerFactory factory;

    @Inject
    public EntityManagerProducer(@PersistenceUnitName("alexis") EntityManagerFactory factory) {
        this.factory = Objects.requireNonNull(factory);
    }

    /**
     * This is {@link RequestScoped} as the underlying implementation
     * is not thread safe when interacting with same table in multiple threads.
     * So circumvent this we create en {@link EntityManager} per request.
     *
     * @return A new instance of the EntityManager from {@link #factory}.
     */
    @Produces
    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public void close(@Disposes EntityManager manager) {
        if (manager != null && manager.isOpen())
            manager.close();
    }

    @PreDestroy
    @Override
    public void close() {
        if (factory.isOpen())
            factory.close();
    }
}
