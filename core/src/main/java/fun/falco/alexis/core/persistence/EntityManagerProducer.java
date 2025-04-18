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

package fun.falco.alexis.core.persistence;

import java.io.Closeable;
import java.util.Objects;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

/**
 * @author seth@falco.fun (Seth Falco)
 * @since 3.0.0
 */
@ApplicationScoped
public class EntityManagerProducer implements Closeable {

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
     * @return New instance of the EntityManager from {@link #factory}.
     */
    @Produces
    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public void close(@Disposes EntityManager manager) {
        if (manager != null && manager.isOpen()) {
            manager.close();
        }
    }

    @PreDestroy
    @Override
    public void close() {
        if (factory.isOpen()) {
            factory.close();
        }
    }
}
