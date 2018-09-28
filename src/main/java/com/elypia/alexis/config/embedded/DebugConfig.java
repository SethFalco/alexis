package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

public class DebugConfig {

    @Path("db_enabled")
    private boolean databaseEnabled;

    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }
}
