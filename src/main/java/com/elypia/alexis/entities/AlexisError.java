package com.elypia.alexis.entities;

import com.elypia.alexis.entities.impl.DatabaseEntity;
import org.bson.types.ObjectId;
import xyz.morphia.annotations.*;

import java.util.Date;

@Entity(value = "errors", noClassnameStored = true)
public class AlexisError implements DatabaseEntity {

    @Id
    private ObjectId id;

    @Property("timestamp")
    private Date timestamp;

    @Property("stacktrace")
    private String stacktrace;

    public AlexisError(String stacktrace) {
        timestamp = new Date();
        this.stacktrace = stacktrace;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
