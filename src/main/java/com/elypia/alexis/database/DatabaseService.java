package com.elypia.alexis.database;

import com.elypia.alexis.config.DatabaseConfig;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

/**
 * The database service is created on runtime regardless, however
 * if it is actively connected to a live database can be configured with the
 * {@link DatabaseConfig#isEnabled()} method.
 */
@Singleton
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    private boolean isEnabled;

    @Inject
    public DatabaseService(final DatabaseConfig config) {
        Objects.requireNonNull(config);
        isEnabled = config.isEnabled();

        if (!isEnabled)
            return;

        // TODO: Login to database
        logger.info("Successfully initialised instance of {} and connected to database.", this.getClass().getSimpleName());
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
