package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

public class Author {

    @Path("id")
    private long id;

    @Path("role")
    private String role;

    @Path("url")
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
