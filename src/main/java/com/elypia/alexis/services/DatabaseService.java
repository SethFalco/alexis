/*
 * Copyright (C) 2019  Elypia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.elypia.alexis.services;

import com.elypia.alexis.configuration.DatabaseConfig;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

/**
 * The database service is created on runtime regardless, however
 * if it is actively connected to a live database can be configured with the
 * {@link DatabaseConfig#isEnabled()} method.
 */
@Singleton
public class DatabaseService implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    private final boolean isEnabled;

    /** Creates the session factory so we can create transactions and commit changes. */
    private SessionFactory sessionFactory;

    @Inject
    public DatabaseService(final DatabaseConfig config) {
        Objects.requireNonNull(config);
        isEnabled = config.isEnabled();

        if (!isEnabled)
            return;

        sessionFactory = new Configuration()
            .setProperty("javax.persistence.jdbc.password", config.getPassword())
            .configure()
            .buildSessionFactory();

        logger.info("Succesfully connected to database.");
    }

    public Session open() {
        return sessionFactory.openSession();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
