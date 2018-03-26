package com.elypia.alexis.utils;

import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.elypiai.osu.data.OsuMode;
import com.elypia.elypiai.utils.Regex;
import net.dv8tion.jda.core.entities.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collection;
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

            else if (clazz == TextChannel[].class)
                return parseChannelArray(event, input);

            else if (clazz == VoiceChannel[].class)
                return parseVoiceArray(event, input);

            else if (clazz == Role[].class)
                return parseRoleArray(event, input);

            else if (clazz == Emote[].class)
                return parseEmoteArray(event, input);

            else if (clazz == OsuMode[].class)
                return parseOsuModeArray(input);
        } else {
            if (object.getClass().isArray()) {
                String arg = String.join(", ", (String[])object);
                throw new IllegalArgumentException("Parameter `" + arg + "` can't be a list.");
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

                else if (clazz == TextChannel.class)
                    return parseChannel(event, input);

                else if (clazz == VoiceChannel.class)
                    return parseVoice(event, input);

                else if (clazz == Role.class)
                    return parseRole(event, input);

                else if (clazz == Emote.class)
                    return parseEmote(event, input);

                else if (clazz == OsuMode.class)
                    return parseOsuMode(input);
            }
        }

        BotUtils.LOGGER.log(Level.SEVERE, "A module is demanding a parameter which can't be parsed.");
        throw new IllegalArgumentException("Sorry, this command was made incorrectly.");
    }

    private static int parseInt(String input) throws IllegalArgumentException {
        if (!Regex.NUMBER.matches(input))
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");

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
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");

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
            throw new IllegalArgumentException("Parameter `" + input + "` is not a number.");

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
            throw new IllegalArgumentException("Parameter `" + input + "` is not a valid URL.");
        }
    }

    private static URL[] parseUrlArray(String[] input) throws IllegalArgumentException {
        URL[] output = new URL[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseUrl(input[i]);

        return output;
    }

    private static OffsetDateTime parseTime(String input) throws IllegalArgumentException {
        throw new IllegalArgumentException("Parameter `" + input + "` could not be parsed as a time.");
    }

    private static OffsetDateTime[] parseTimeArray(String[] input) throws IllegalArgumentException {
        OffsetDateTime[] output = new OffsetDateTime[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseTime(input[i]);

        return output;
    }

    private static Guild parseGuild(MessageEvent event, String input) throws IllegalArgumentException {
        Collection<Guild> guilds = event.getAuthor().getMutualGuilds();

        for (Guild g : guilds) {
            if (g.getId().equals(input) || g.getName().equalsIgnoreCase(input))
                return g;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a guild.");
    }

    private static Guild[] parseGuildArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Guild[] output = new Guild[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseGuild(event, input[i]);

        return output;
    }

    private static Member parseMember(MessageEvent event, String input) throws IllegalArgumentException {
        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a member.");
    }

    private static Member[] parseMemberArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Member[] output = new Member[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseMember(event, input[i]);

        return output;
    }

    private static User parseUser(MessageEvent event, String input) throws IllegalArgumentException {

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a user.");
    }

    private static User[] parseUserArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        User[] output = new User[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseUser(event, input[i]);

        return output;
    }

    private static TextChannel parseChannel(MessageEvent event, String input) throws IllegalArgumentException {
        Collection<TextChannel> channels = event.getGuild().getTextChannels();

        for (TextChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input) || channel.getAsMention().equals(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }

    private static TextChannel[] parseChannelArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        TextChannel[] output = new TextChannel[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseChannel(event, input[i]);

        return output;
    }

    private static VoiceChannel parseVoice(MessageEvent event, String input) throws IllegalArgumentException {
        Collection<VoiceChannel> channels = event.getGuild().getVoiceChannels();

        for (VoiceChannel channel : channels) {
            if (channel.getId().equals(input) || channel.getName().equalsIgnoreCase(input))
                return channel;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a channel.");
    }

    private static VoiceChannel[] parseVoiceArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        VoiceChannel[] output = new VoiceChannel[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseVoice(event, input[i]);

        return output;
    }

    private static Role parseRole(MessageEvent event, String input) throws IllegalArgumentException {
        Collection<Role> roles = event.getGuild().getRoles();

        for (Role role : roles) {
            if (role.getId().equals(input) || role.getAsMention().equals(input) || role.getName().equalsIgnoreCase(input))
                return role;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a role.");
    }

    private static Role[] parseRoleArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Role[] output = new Role[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseRole(event, input[i]);

        return output;
    }

    private static Emote parseEmote(MessageEvent event, String input) throws IllegalArgumentException {
        Collection<Emote> emotes = event.getJDA().getEmotes();

        for (Emote emote : emotes) {
            if (emote.getId().equals(input) || emote.getAsMention().equals(input) || emote.getName().equalsIgnoreCase(input))
                return emote;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to an emote.");
    }

    private static Emote[] parseEmoteArray(MessageEvent event, String[] input) throws IllegalArgumentException {
        Emote[] output = new Emote[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseEmote(event, input[i]);

        return output;
    }

    private static OsuMode parseOsuMode(String input) throws IllegalArgumentException {
        for (OsuMode mode : OsuMode.values()) {
            if (input.equals(String.valueOf(mode.getId())))
                return mode;
        }

        if (input.equalsIgnoreCase("osu"))
            return OsuMode.OSU;

        if (input.equalsIgnoreCase("mania") || input.equalsIgnoreCase("piano"))
            return OsuMode.MANIA;

        if (input.equalsIgnoreCase("taiko") || input.equalsIgnoreCase("drums"))
            return OsuMode.TAIKO;

        if (input.equalsIgnoreCase("catch the beat") || input.equalsIgnoreCase("ctb"))
            return OsuMode.CATCH_THE_BEAT;

        throw new IllegalArgumentException("Parameter `" + input + "` isn't a game mode on osu!");
    }

    private static OsuMode[] parseOsuModeArray(String[] input) throws IllegalArgumentException {
        OsuMode[] output = new OsuMode[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = parseOsuMode(input[i]);

        return output;
    }
}
