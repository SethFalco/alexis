/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
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

package org.elypia.alexis.services;

import org.elypia.alexis.config.DatabaseConfig;
import org.elypia.alexis.entities.*;
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

        configuration.addAnnotatedClass(ActivityData.class);
        configuration.addAnnotatedClass(AssignableRole.class);
        configuration.addAnnotatedClass(EmoteData.class);
        configuration.addAnnotatedClass(EmoteUsage.class);
//        configuration.addAnnotatedClass(GuildData.class);
//        configuration.addAnnotatedClass(GuildFeature.class);
        configuration.addAnnotatedClass(LogSubscription.class);
        configuration.addAnnotatedClass(MemberData.class);
        configuration.addAnnotatedClass(MemberSkill.class);
        configuration.addAnnotatedClass(MessageChannelData.class);
        configuration.addAnnotatedClass(GuildMessage.class);
        configuration.addAnnotatedClass(Milestone.class);
        configuration.addAnnotatedClass(SkillData.class);
        configuration.addAnnotatedClass(UserData.class);

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
