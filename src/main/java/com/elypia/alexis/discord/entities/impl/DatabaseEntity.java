package com.elypia.alexis.discord.entities.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

/**
 * This interface is <strong>ONLY</strong> for objects that
 * wrap around a collection as a whole, and should not be
 * be inheritted for smaller objects that are a part of a larger one.
 * For exmaple, guilds, and users. However not for tags or textchannels.
 */

public abstract class DatabaseEntity {

    protected MongoDatabase database;
    protected MongoCollection<Document> collection;
    protected UpdateOptions options;

    protected DatabaseEntity(MongoDatabase database, String collection) {
        this.database = database;
        this.collection = database.getCollection(collection);

        options = new UpdateOptions();
        options.upsert(true);
    }

    /**
     * Commit any changes that have been made so far
     * to the database.
     */

    protected abstract void commit();

    /**
     * @param data The name of the column / data in the database.
     * @param value The id of the entry we're after.
     * @param <T> How the fuck do I document this?
     * @return The database entry for this unique id, or <strong>null</strong> if nothing was found.
     */

    protected <T> Document getById(String data, T value) {
        return collection.find(eq(data, value)).first();
    }

    /**
     * Generic function to collect JSONArray from query result.
     *
     * @param document The parents data entity.
     * @param array The name of the JSONArray.
     * @param target What to do with the result on collection.
     * @param <T> ...
     */

    protected <T> void getArray(Document document, String array, Consumer<T> target) {
        List<T> list = document.get(array, List.class);
        list.forEach(target::accept);
    }
}
