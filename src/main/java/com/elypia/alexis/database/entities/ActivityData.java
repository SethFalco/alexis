package com.elypia.alexis.database.entities;

import javax.persistence.*;

@Entity(name = "status")
@Table
public class ActivityData {

    @Column(name = "status_type")
    private int type;

    @Column(name = "status_text")
    private String text;

    @Column(name = "status_url")
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
