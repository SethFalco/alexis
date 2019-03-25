package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.Path;

public class DatabaseConfig {

    @Path("ip")
    private String ip;

    @Path("port")
    private int port;

    @Path("user")
    private String user;

    @Path("password")
    private String password;

    @Path("database")
    private String database;

    public DatabaseConfig() {

    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
