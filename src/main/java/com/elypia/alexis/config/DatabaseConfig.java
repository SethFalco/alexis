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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
