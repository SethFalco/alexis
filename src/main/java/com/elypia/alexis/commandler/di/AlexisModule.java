package com.elypia.alexis.commandler.di;

import java.util.*;

public class AlexisModule extends AbstractModule {

    private Map<Class, Object> bindings;

    public AlexisModule() {
        bindings = new HashMap<>();
    }

    public <T> void bind(Class<T> clazz, T object) {
        bindings.put(clazz, object);
    }

    @Override
    public void configure() {
        bindings.forEach((clazz, object) -> {
            bind(clazz).toInstance(object);
        });
    }
}
