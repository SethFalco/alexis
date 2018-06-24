package com.elypia.alexis.config.deserializers;

import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import com.google.gson.*;

import java.lang.reflect.Type;

public class AmazonEndpointDeserializer implements JsonDeserializer<AmazonEndpoint> {

    @Override
    public AmazonEndpoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonNull()) {
            String endpoint = json.getAsString();

            for (AmazonEndpoint amazonEndpoint : AmazonEndpoint.values()) {
                if (amazonEndpoint.name().equalsIgnoreCase(endpoint))
                    return amazonEndpoint;
            }
        }

        return null;
    }
}
