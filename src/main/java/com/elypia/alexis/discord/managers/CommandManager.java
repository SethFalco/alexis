package com.elypia.alexis.discord.managers;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.CommandGroup;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.discord.managers.impl.DiscordManager;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.utils.ElyUtils;
import com.elypia.elypiai.utils.Regex;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Received and performs all message or reactions
 * that refer to commands for this chatbot.
 * See {@link EventManager} to non-command related handling.
 */

public class CommandManager extends DiscordManager {

    /**
     * A all registers handlers for this instance of the chatbot. <br>
     * The key is an array of aliases that refer to a module. <br>
     * The value is the module itself.
     */

    private Map<String[], CommandHandler> handlers;

    public CommandManager(Chatbot chatbot) {
        super(chatbot);

        handlers = new HashMap<>();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!canPerform(event.getAuthor()))
            return;

        handleMessage(new MessageEvent(chatbot, event));
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

    }

    /**
     * Checks if the the user that triggered the event is allowed to perform
     * to do commands at the moment.
     *
     * @param user The user that just triggered this event.
     * @return If the user can perform commands on this instance of the chatbot.
     */

    private boolean canPerform(User user) {
        if (user.isBot())
            return false;

        if (config.developersOnly())
            return config.isDeveloper(user);

        return true;
    }

    /**
     * Register multiple handlers.
     *
     * @param handlers
     */

    public void registerModules(CommandHandler... handlers) {
        for (CommandHandler handler : handlers)
            registerModule(handler);
    }

    public void registerModule(CommandHandler handler) {
        Module module = handler.getClass().getAnnotation(Module.class);

        if (module == null) {
            String message = "{0} doesn't include the @Module annotation!";
            BotUtils.LOGGER.log(Level.SEVERE, message, handler);
            return;
        }

        String[] aliases = module.aliases();

        for (int i = 0; i < aliases.length; i++)
            aliases[i] = aliases[i].toLowerCase();

        for (String[] existing : handlers.keySet()) {
            if (ElyUtils.containsAny(existing, aliases)) {
                String message = "{0} contains an alias which has already been registered!";
                BotUtils.LOGGER.log(Level.WARNING, message, handler);
                return;
            }
        }

        handlers.put(aliases, handler);
    }

    private void handleMessage(MessageEvent event) {
        if (!event.isValid())
            return;

        BotUtils.LOGGER.log(Level.INFO, event.getMessage().getContentRaw());

        CommandHandler handler = getHandler(event);

        if (handler == null)
            return;

        Collection<Method> commands = getCommandGroup(event, handler);
        Method method = filterByParamCount(event, commands);

        if (method == null) {
            event.reply("I couldn't find a way to do this with the number of parameteres you provided. Maybe try help");
            return;
        }

        Object[] params = parseParameters(event, method);

        try {
            method.invoke(handler, params);
//            event.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
            BotUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
        }
    }

    private CommandHandler getHandler(MessageEvent event) {
        String module = event.getModule();

        for (String[] aliases : handlers.keySet()) {
            if (ElyUtils.arrayContains(module, aliases))
                return handlers.get(aliases);
        }

        return null;
    }

    private Object[] parseParameters(MessageEvent event, Method method) throws IllegalArgumentException {
        Object[] inputs = event.getParams();
        Class<?>[] types = method.getParameterTypes();
        Object[] params = new Object[types.length];

        params[0] = event;

        for (int i = 1; i <= inputs.length; i++) {
            Object input = inputs[i - 1];
            Class<?> type = types[i];

            if (type == String.class)
                params[i] = Objects.toString(input);

            else if (type == int.class) {
                if (Regex.NUMBER.matches(input.toString()))
                    params[i] = Integer.parseInt(input.toString());
                else
                    throw new IllegalArgumentException("One of the parameters provided was not a number.");
            }

            else if (type == Instant.class) {

            }

            else if (type == boolean.class) {
                params[i] = parseBoolean(input.toString());
            }
        }

        return params;
    }

    public boolean parseBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("yes");
    }

    private Method filterByParamCount(MessageEvent event, Collection<Method> methods) {
        methods = methods.stream().filter(o -> o.getParameterCount() - 1 == event.getParams().length).collect(Collectors.toList());
        return methods.iterator().next();
    }

    private Collection<Method> getCommandGroup(MessageEvent event, CommandHandler handler) {
        Collection<Method> thisCommand = new ArrayList<>();
        Collection<Method> methods = getCommands(event, handler);
        String command = event.getCommand();
        CommandGroup group = null;

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);

            if (annotation != null) {
                if (arrayContains(annotation.aliases(), command)) {
                    group = method.getAnnotation(CommandGroup.class);
                    thisCommand.add(method);
                    break;
                }
            }
        }

        if (group != null) {
            for (Method method : methods) {
                CommandGroup annotation = method.getAnnotation(CommandGroup.class);

                if (annotation != null) {
                    if (annotation.value().equals(group.value()))
                        thisCommand.add(method);
                }
            }
        }

        return thisCommand;
    }

    private boolean arrayContains(String[] array, String command) {
        for (String string : array) {
            if (string.equalsIgnoreCase(command))
                return true;
        }

        return false;
    }

    private Collection<Method> getCommands(MessageEvent event, CommandHandler handler) {
        Collection<Method> commands = new ArrayList<>();
        Method[] methods = handler.getClass().getMethods();

        for (Method method : methods) {
            if (isCommand(method))
                commands.add(method);
        }

        return commands;
    }

    private boolean isCommand(Method method) {
        return method.getAnnotation(Command.class) != null || method.getAnnotation(CommandGroup.class) != null;
    }
}
