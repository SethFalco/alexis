package com.elypia.alexis.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;

public class Config {

    private static JSONObject config;

    private Config() {

    }

    public static void init(String location) throws IOException {
        Path path = Paths.get(location);
        byte[] bytes = Files.readAllBytes(path);
        String content = new String(bytes, "UTF-8");
        config = new JSONObject(content);
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static JSONObject getConfig(String key) {
        return config.getJSONObject(key);
    }
}
