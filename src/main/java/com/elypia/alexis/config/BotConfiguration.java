package com.elypia.alexis.config;

import com.elypia.alexis.config.deserializers.*;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.core.entities.Game;

import java.io.IOException;
import java.nio.file.*;

public class BotConfiguration {

    @SerializedName("database")
    private DatabaseConfig databaseConfig;

    @SerializedName("discord")
    private DiscordConfig discordConfig;

    @SerializedName("api_keys")
    private ApiKeys apiKeys;

    public static BotConfiguration instance(String path) {
        String text = null;

        try {
            text = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Game.class, new GameDeserializer());
        builder.registerTypeAdapter(AmazonEndpoint.class, new AmazonEndpointDeserializer());
        Gson gson = builder.create();

        return gson.fromJson(text, BotConfiguration.class);
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
