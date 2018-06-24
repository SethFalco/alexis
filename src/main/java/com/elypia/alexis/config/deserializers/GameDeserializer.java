package com.elypia.alexis.config.deserializers;

import com.google.gson.*;
import net.dv8tion.jda.core.entities.Game;

import java.lang.reflect.Type;

public class GameDeserializer implements JsonDeserializer<Game> {

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonNull()) {
            JsonObject object = json.getAsJsonObject();
            return Game.of(Game.GameType.fromKey(object.get("key").getAsInt()), object.get("text").getAsString());
        }

        return null;
    }
}
