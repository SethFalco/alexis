package com.elypia.alexis.utils;

import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.elypiai.utils.Regex;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.logging.Level;

public final class ParamParser {

    private static final String[] BOOLEAN = {"true", "yes", "1", "✔"};

    public static Object parseParam(MessageEvent event, Object object, Class<?> clazz) throws IllegalArgumentException {
        if (clazz.isArray()) {
            if (!object.getClass().isArray())
                object = new String[] {(String)object};

            String[] input = (String[])object;

            if (clazz == int[].class)
                return parseIntArray(input);

            else if (clazz == long[].class)
                return parseLongArray(input);

            else if (clazz == double[].class)
                return parseDoubleArray(input);

            else if (clazz == boolean[].class)
                return parseBooleanArray(input);

            else if (clazz == String[].class)
                return input;

            else if (clazz == URL[].class)
                return parseUrlArray(input);

            else if (clazz == Instant[].class)
                return parseTimeArray(input);

            else if (clazz == Guild[].class)
                return parseGuildArray(event, input);

            else if (clazz == Member[].class)
                return parseMemberArray(event, input);

            else if (clazz == User[].class)
                return parseUserArray(event, input);

            else if (clazz == Role[].class)
                return parseRoleArray(event, input);
        } else {
            if (object.getClass().isArray()) {
                String arg = String.join(", ", (String[])object);
                throw new IllegalArgumentException("Parameter " + arg + " can't be as a list.");
            }

            String input = (String)object;

            if (clazz.isPrimitive()) {
                if (clazz == int.class)
                    return parseInt(input);

                else if (clazz == long.class)
                    return parseLong(input);

                else if (clazz == double.class)
                    return parseDouble(input);

                else if (clazz == boolean.class)
                    return parseBoolean(input);
            } else {
                if (clazz == String.class)
                    return input;

                else if (clazz == URL.class)
                    return parseUrl(input);

                else if (clazz == Instant.class)
                    return parseTime(input);

                else if (clazz == Guild.class)
                    return parseGuild(event, input);

                else if (clazz == Member.class)
                    return parseMember(event, input);

                else if (clazz == User.class)
                    return parseUser(event, input);

                else if (clazz == Role.class)
                    return parseRole(event, input);
            }
        }

        BotUtils.LOGGER.log(Level.SEVERE, "A module is demanding a parameter which can't be parsed.");
        throw new IllegalArgumentException("Sorry, this command was made incorrectly.");
    }

    private static int parseInt(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter " + input + " is not a number.");

        return Integer.parseInt(input);
    }

    private static int[] parseIntArray(String[] input) throws IllegalArgumentException {
        int[] output = new int[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseInt(input[i]);

        return output;
    }

    private static long parseLong(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter " + input + " is not a number.");

        return Long.parseLong(input);
    }

    private static long[] parseLongArray(String[] input) throws IllegalArgumentException {
        long[] output = new long[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseLong(input[i]);

        return output;
    }

    private static double parseDouble(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter " + input + " is not a number.");

        return Double.parseDouble(input);
    }

    private static double[] parseDoubleArray(String[] input) throws IllegalArgumentException {
        double[] output = new double[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseDouble(input[i]);

        return output;
    }

    private static boolean parseBoolean(String input) throws IllegalArgumentException {
        for (String bool : BOOLEAN) {
            if (bool.equalsIgnoreCase(input))
                return true;
        }

        return false;
    }

    private static boolean[] parseBooleanArray(String[] input) throws IllegalArgumentException {
        boolean[] output = new boolean[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseBoolean(input[i]);

        return output;
    }

    private static URL parseUrl(String input) throws IllegalArgumentException {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter " + input + " is not a valid URL.");
        }
    }

    private static URL[] parseUrlArray(String[] input) throws IllegalArgumentException {
        URL[] output = new URL[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseUrl(input[i]);

        return output;
    }

    private static OffsetDateTime parseTime(String input) throws IllegalArgumentException {
        return null;
    }

    private static OffsetDateTime[] parseTimeArray(String[] input) throws IllegalArgumentException {
        OffsetDateTime[] output = new OffsetDateTime[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseTime(input[i]);

        return output;
    }

    private static Guild parseGuild(MessageEvent event, String input) throws IllegalArgumentException {
        return null;
    }

    private static Guild[] parseGuildArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Guild[] output = new Guild[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseGuild(event, input[i]);

        return output;
    }

    private static Member parseMember(MessageEvent event, String input) throws IllegalArgumentException {
        return null;
    }

    private static Member[] parseMemberArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Member[] output = new Member[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseMember(event, input[i]);

        return output;
    }

    private static User parseUser(MessageEvent event, String input) throws IllegalArgumentException {
        return null;
    }

    private static User[] parseUserArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        User[] output = new User[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseUser(event, input[i]);

        return output;
    }

    private static Role parseRole(MessageEvent event, String input) throws IllegalArgumentException {
        return null;
    }

    private static Role[] parseRoleArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Role[] output = new Role[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseRole(event, input[i]);

        return output;
    }
}
