package com.elypia.alexis.entities.impl;

import com.elypia.alexis.Alexis;

public interface DatabaseEntity {

    default void commit() {
        Alexis.getDatabaseManager().commit(this);
    }
}
