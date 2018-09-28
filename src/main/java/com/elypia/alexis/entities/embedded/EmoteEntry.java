package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

import java.util.Date;

@Embedded
public class EmoteEntry {

    @Property("date")
    private Date date;

    @Property("count")
    private int count;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
