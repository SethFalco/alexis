package com.elypia.alexis.configuration;

import com.electronwill.nightconfig.core.conversion.*;

import javax.inject.Singleton;

@Singleton
public class DatabaseConfig {

    /** Should database functionaly be enabled. This must be specified. */
    @Path("enabled")
    @SpecNotNull
    private boolean enabled;

    @Path("password")
    private String password;

    public boolean isEnabled() {
        return enabled;
    }

    public String getPassword() {
        return password;
    }
}
