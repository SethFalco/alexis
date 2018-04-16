package com.elypia.commandler.parsing;

import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.commandler.parsing.impl.Parser;

import java.util.HashMap;
import java.util.Map;

public class ParamParser {

    private Map<Class<?>, Parser> parsers;

    public ParamParser() {
        parsers = new HashMap<>();
    }

    public <T> void registerParser(Class<T> t, Parser<T> parser) {
        if (parsers.keySet().contains(t))
            throw new IllegalArgumentException("Parser for this type of object has already been registred.");

        parsers.put(t, parser);
    }

    public static Object parseParam(MessageEvent event, Object object, Class<?> clazz) throws IllegalArgumentException {
        boolean array = object.getClass().isArray();

        if (clazz.isArray()) {
            String[] input = array ? (String[])object : new String[] {(String)object};

            // temp
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list.");

            String input = (String)object;

            // temp
        }

        throw new IllegalArgumentException("Sorry, this command was made incorrectly.");
    }
}
