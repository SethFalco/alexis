package com.elypia.alexis.managers;

import com.elypia.alexis.config.embedded.DatabaseConfig;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import com.mongodb.MongoClient;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.Query;
import org.slf4j.*;

import java.util.*;

public class DatabaseManager {

    private static final String DISABLED_WARNING = "Database access is disabled in configuration file, ignoring request to initialise database.";

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    /**
     * The connection to the MongoDB database which stores everything Alexis
     * requires.
     */
    private MongoClient client;

    /**
     * Morphia is a library which allows us to map objects from the database to
     * Java objects one to one, this is ues to make it cleaner to query and update the database.
     */
    private Morphia morphia;

    /**
     * A database store is used to perform database queries and updates.
     */
    private Datastore store;

    private final DatabaseConfig config;

    public DatabaseManager(final DatabaseConfig config) {
        this.config = Objects.requireNonNull(config);

//        String ip = config.getIp();
//        int port = config.getPort();
//        ServerAddress address = new ServerAddress(ip, port);
//
//        String user = config.getUser();
//
//        if (user == null)
//            user = "";
//
//        String source = config.getDatabase();
//
//        String password = config.getPassword();
//
//        if (password == null)
//            password = "";
//
//        MongoCredential credentials = MongoCredential.createCredential(user, source, password.toCharArray());
//
//        var settings = MongoClientSettings.builder().applyToClusterSettings(builder -> {
//            builder.hosts(List.of(address));
//        }).credential(credentials).build();
//
//        client = MongoClients.create(settings);

        MongoClient client = new MongoClient(config.getIp(), config.getPort());

        morphia = new Morphia();
        morphia.mapPackage("com.elypia.alexis.entities");

        store = morphia.createDatastore(client, config.getDatabase());
        store.ensureIndexes();
    }

    public <T> T query(Class<T> type, String key, Object value) {
        return query(type, Map.of(key, value));
    }

    public <T> T query(Class<T> type, Map<String, Object> params) {
        Query<T> query = store.createQuery(type);
        params.forEach((key, value) -> query.filter(key + " ==", value));
        return query.get();
    }

    public void commit(DatabaseEntity entity) {
        store.save(entity);
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
