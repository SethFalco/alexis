package com.elypia.alexis.config.converters;

import com.electronwill.nightconfig.core.conversion.Converter;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;

public class AmazonEndpointConverter implements Converter<AmazonEndpoint, String> {

    @Override
    public AmazonEndpoint convertToField(String value) {
        for (AmazonEndpoint endpoint : AmazonEndpoint.values()) {
            if (endpoint == AmazonEndpoint.UNKNOWN)
                continue;

            if (endpoint.name().equalsIgnoreCase(value))
                return endpoint;

            if (endpoint.getTld().equals(value))
                return endpoint;

            if (endpoint.getEndpoint().toString().equalsIgnoreCase(value))
                return endpoint;

            if (endpoint.getShoppingUrl().equalsIgnoreCase(value))
                return endpoint;
        }

        return null;
    }

    @Override
    public String convertFromField(AmazonEndpoint value) {
        return value.name();
    }
}
