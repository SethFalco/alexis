package com.elypia.alexis.config;

import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("id")
    private long id;

    @SerializedName("role")
    private String role;

    @SerializedName("url")
    private String url;

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getUrl() {
        return url;
    }
}
