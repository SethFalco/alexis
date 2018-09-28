package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.*;

import java.util.List;

public class DiscordConfig {

    @Path("token")
    @SpecNotNull
    private String token;

    @Path("prefix")
    @SpecNotNull
    private String prefix;

    @Path("support_guild")
    private long supportGuild;

    // ! We are temp loading only 1 Author until the bug in NightConfig is fixed.
    @Path("author")
    private Author authors;

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getSupportGuild() {
        return supportGuild;
    }

    public List<Author> getAuthors() {
        return List.of(authors);
    }
}
