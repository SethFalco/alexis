package com.elypia.alexis.database;

import com.elypia.alexis.config.embedded.DatabaseConfig;
import org.slf4j.*;

import java.util.Objects;

public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    private final DatabaseConfig config;

    public DatabaseService(final DatabaseConfig config) {
        this.config = Objects.requireNonNull(config);


        logger.info("Succesfully initialised instance of {} and connected to database.", this.getClass().getSimpleName());
    }
}
