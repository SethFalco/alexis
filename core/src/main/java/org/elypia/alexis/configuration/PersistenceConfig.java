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

package org.elypia.alexis.configuration;

import org.apache.deltaspike.core.api.config.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Configuration(prefix = "application.persistence.")
public interface PersistenceConfig {

    @ConfigProperty(name = "dialect", defaultValue = "org.hibernate.dialect.H2Dialect")
    String dialect();

    /** The host server this database server is running on. */
    @ConfigProperty(name = "url", defaultValue = "jdbc:h2:file:./persistence/alexis")
    String getUrl();

    /** The username for this database user. */
    @ConfigProperty(name = "username")
    String getUsername();

    /** Password to the Alexis user on the database. */
    @ConfigProperty(name = "password")
    String getPassword();

    @ConfigProperty(name = "driver", defaultValue = "org.h2.Driver")
    String driver();
}
