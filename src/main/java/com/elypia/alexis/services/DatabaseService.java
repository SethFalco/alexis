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

    /** Creates the session factory so we can create transcation and commit changes. */
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

    @Override
    public void close() {
        sessionFactory.close();
    }
}
