package com.elypia.alexis.config;

import com.google.gson.annotations.SerializedName;

public class DatabaseConfig {

    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("ip")
    private String ip;

    @SerializedName("port")
    private int port;

    @SerializedName("database")
    private String database;

    @SerializedName("user")
    private String user;

    @SerializedName("password")
    private String password;

    public boolean isEnabled() {
        return enabled;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
