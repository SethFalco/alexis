package com.elypia.alexis.config;

import com.elypia.alexis.config.deserializers.GameDeserializer;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.core.entities.Game;

import java.io.IOException;
import java.nio.file.*;

public class AlexisConfig {

    @SerializedName("database")
    private DatabaseConfig databaseConfig;

    @SerializedName("discord")
    private DiscordConfig discordConfig;

    @SerializedName("api_keys")
    private ApiKeys apiKeys;

    public static AlexisConfig initConfig(String path) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(path)));

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Game.class, new GameDeserializer());
//        builder.registerTypeAdapter(AmazonEndpoint.class, new AmazonEndpointDeserializer());
        Gson gson = builder.create();

        return gson.fromJson(text, AlexisConfig.class);
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public DiscordConfig getDiscordConfig() {
        return discordConfig;
    }

    public ApiKeys getApiKeys() {
        return apiKeys;
    }
}
