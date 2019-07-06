package com.elypia.alexis.database;

import com.elypia.alexis.config.DatabaseConfig;
import org.slf4j.*;

import javax.inject.*;
import java.util.Objects;

@Singleton
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    private final DatabaseConfig config;

    @Inject
    public DatabaseService(final DatabaseConfig config) {
        this.config = Objects.requireNonNull(config);
        // TODO: Login to database
        logger.info("Successfully initialised instance of {} and connected to database.", this.getClass().getSimpleName());
    }
}
