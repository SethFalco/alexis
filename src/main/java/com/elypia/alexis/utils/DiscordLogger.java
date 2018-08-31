package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.DiscordConfig;
import com.elypia.commandler.jda.JDACommand;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;
import java.util.logging.*;

public final class DiscordLogger {

    /**
     * A predefined list of levels which will allow us to log to
     * Discord if the logging level is set to one of these. <br>
     * (We just do this 'cause it's probably faster to do a contains check.)<br>
     * <br>
     * ? WARNING: Log and mention with red text.
     * ? INFO: Log with red text.
     * ? FINE: Log with green text.
     */
    private static final Collection<Level> DISCORD_LEVELS = List.of(Level.WARNING, Level.INFO, Level.FINE);

    /**
     * This is the default level we'll apply when we're logging an Exception.
     * The default Exception level should log to console, and in the configured
     * {@link DiscordConfig#getLogChannel()} and mention the lead
     * {@link DiscordConfig#getAuthors() developer}.
     */
    private static final Level EXCEPTION_LEVEL = Level.WARNING;

    /**
     * When making a diff, lines prepended with a
     * <strong>+</strong> appear in green.
     */
    private static final String GREEN = "+";

    /**
     * When making a diff, lines prepended with a
     * <strong>-</strong> appear in red.
     */
    private static final String RED = "-";

    /**
     * The native Java {@link Logger} instance to produce logs.
     */
    private static Logger logger = Logger.getLogger(Alexis.class.getName());


    private DiscordLogger() {
        // ! Don't construct this!
    }

    public static void log(Throwable throwable) {
        log((Event)null, throwable);
    }

    public static void log(JDACommand event, Throwable throwable) {
        log(event.getSource(), throwable);
    }

    public static void log(Event event, Throwable throwable) {
        // Log stack trace to console.
        throwable.printStackTrace();

        // Log stack trace to Discord.
        log(event, EXCEPTION_LEVEL, ExceptionUtils.getStackTrace(throwable), new Object[0], false);

        // ! Readd something to message the user again and apologise.
    }

    public static void log(JDACommand event, Level level, String message, Object... args) {
        log(event.getSource(), level, message, args);
    }

    public static void log(Event event, Level level, String message, Object... args) {
        log(event, level, message, args, true);
    }

    private static void log(Event event, Level level, String message, Object[] args, boolean print) {
        message = String.format(message, args);

        if (print)
            logger.log(level, message);

        if (DISCORD_LEVELS.contains(level)) {
            String color = (level == Level.FINE) ? GREEN : RED;

            message = "```diff\n" + level.getName() + ":\n" + message + "```";
            message = message.replace("\n", "\n" + color + " ");

            MessageChannel channel = getLogChannel(event.getJDA());

            if (level == Level.WARNING) {
                long userId = Alexis.config.getDiscordConfig().getAuthors().get(0).getId();
                User user = Alexis.jda.getUserById(userId);
                message = "_ _\n" + user.getAsMention() + "\n\n" + message;
            }

            channel.sendMessage(message).queue();
        }
    }

    /**
     * This does not log to console.
     */

    public static void log(JDA jda) {
        EmbedBuilder builder = new EmbedBuilder();
        String totalGuilds = String.format("%,d", jda.getGuilds().size());
        builder.addField("Total Guilds", totalGuilds, true);

        List<User> users = jda.getUsers();
        int totalBots = (int)users.stream().filter(User::isBot).count();
        int totalUsers = users.size() - totalBots;
        String allusers = String.format("%,d (%,d)", totalUsers, totalBots);
        builder.addField("Total Users (Bots)", allusers, true);

        MessageChannel channel = getLogChannel(jda);

        if (channel instanceof TextChannel)
            builder.setColor(((TextChannel)channel).getGuild().getSelfMember().getColor());

        channel.sendMessage(builder.build()).queue();
    }

    private static MessageChannel getLogChannel(JDA jda) {
        long channelId = Alexis.config.getDiscordConfig().getLogChannel();
        return jda.getTextChannelById(channelId);
    }
}
