package com.elypia.alexis.config.embedded;

import com.electronwill.nightconfig.core.conversion.*;
import com.elypia.alexis.config.converters.AmazonEndpointConverter;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;

public class AmazonCredentials {

    @Path("key")
    private String key;

    @Path("secret")
    private String secret;

    @Path("tag")
    private String tag;

    @Path("endpoint")
    @Conversion(AmazonEndpointConverter.class)
    private AmazonEndpoint endpoint;

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getTag() {
        return tag;
    }

    public AmazonEndpoint getEndpoint() {
        return endpoint;
    }
}
