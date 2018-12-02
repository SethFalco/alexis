package com.elypia.alexis.managers;

import com.elypia.alexis.config.embedded.DatabaseConfig;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import com.mongodb.MongoClient;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.*;
import org.slf4j.*;

import java.util.*;

public class DatabaseManager {

    private static final String INITIALIZED = "Succesfully initialised instance of {} and connected to database.";

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static final String ENTITY_PACKAGE = "com.elypia.alexis.entities";

    private final DatabaseConfig config;

    /**
     * The connection to the MongoDB database which stores everything Alexis
     * requires.
     */
    private final MongoClient client;

    /**
     * Morphia is a library which allows us to map objects from the database to
     * Java objects one to one, this is ues to make it cleaner to query and update the database.
     */
    private final Morphia morphia;

    /**
     * A database store is used to perform database queries and updates.
     */
    private final Datastore store;

    public DatabaseManager(final DatabaseConfig config) {
        this.config = Objects.requireNonNull(config);

        client = new MongoClient(config.getIp(), config.getPort());

        morphia = new Morphia().mapPackage(ENTITY_PACKAGE);
        new ValidationExtension(morphia);

        store = morphia.createDatastore(client, config.getDatabase());
        store.ensureIndexes();

        logger.info(INITIALIZED, this.getClass().getSimpleName());
    }

    /**
     * Query the database using and obtain a single entry.
     *
     * @param type The type of object to query for.
     * @param key The key to filter by.
     * @param value The value this key should <strong>{@link FieldEnd#equal(Object)}</strong>
     * @param <E> The type of <strong>e</strong>tity to query.
     * @param <V> The type of <strong>v</strong>alue to compare to.
     * @return The first entity found in the database that matches.
     */
    public <E, V>  E query(Class<E> type, String key, V value) {
        return query(type, Map.of(key, value));
    }

    public <T> T query(Class<T> type, Map<String, ?> params) {
        Query<T> query = store.createQuery(type);
        params.forEach((key, value) -> query.field(key).equal(value));

        return query.get();
    }

    public void commit(DatabaseEntity entity) {
        store.save(entity);
    }

    public DatabaseConfig getConfig() {
        return config;
    }

    public MongoClient getClient() {
        return client;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDatastore() {
        return store;
    }
}
