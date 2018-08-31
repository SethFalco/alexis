package com.elypia.alexis.entities.impl;

import com.elypia.alexis.Alexis;

public abstract class DatabaseEntity {

    /**
     * Any top level database entity that isn't embedded
     * inside of a document should have a commit method. <br>
     * This will just save the object into the database.
     */
    public void commit() {
        Alexis.store.save(this);
    }
}
