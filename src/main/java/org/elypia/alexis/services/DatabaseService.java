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

package org.elypia.alexis.services;

import org.elypia.alexis.config.DatabaseConfig;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

/**
 * The database service is created on runtime regardless, however
 * if it is actively connected to a live database can be configured with the
 * {@link DatabaseConfig#isEnabled()} method.
 *
 * @author seth@elypia.org (Seth Falco)
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

        String connection = "jdbc:mysql://" + config.getHost() + ":3306/alexis?autoReconnect=true&serverTimezone=UTC&sslMode=VERIFY_CA"
            + "&trustCertificateKeyStoreUrl=" + config.getTrustCertificateKeyStoreUrl() +
            "&trustCertificateKeyStorePassword=" + config.getTrustCertificateKeyStorePassword() +
            "&clientCertificateKeyStoreUrl=" + config.getClientCertificateKeyStoreUrl() +
            "&clientCertificateKeyStorePassword=" + config.getClientCertificateKeyStorePassword();

        Configuration configuration = new Configuration().configure()
            .setProperty("hibernate.connection.url", connection)
            .setProperty("hibernate.connection.password", config.getPassword());

        if (config.getUsername() != null)
            configuration.setProperty("hibernate.connection.username", config.getUsername());

        sessionFactory = configuration.buildSessionFactory();
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
