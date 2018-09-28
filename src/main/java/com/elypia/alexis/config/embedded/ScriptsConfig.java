package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

public class ScriptsConfig {

    @Path("id")
    private String id;

    @Path("range")
    private String range;

    public String getId() {
        return id;
    }

    public String getRange() {
        return range;
    }
}
