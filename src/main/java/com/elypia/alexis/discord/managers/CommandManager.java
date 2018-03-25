package com.elypia.alexis.discord.managers;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.Config;
import com.elypia.alexis.discord.annotations.Developer;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Permissions;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.discord.managers.impl.DiscordManager;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.CommandUtils;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import static com.elypia.alexis.utils.BotUtils.log;

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

    private Map<Collection<String>, CommandHandler> handlers;

    public CommandManager(Chatbot chatbot) {
        super(chatbot);

        handlers = new HashMap<>();
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

        Collection<String> aliases = new ArrayList<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        for (Collection<String> key : handlers.keySet()) {
            if (!Collections.disjoint(key, aliases)) {
                String message = "{0} contains an alias which has already been registered!";
                BotUtils.LOGGER.log(Level.WARNING, message, handler);
            }
        }

        handlers.put(aliases, handler);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (canPerform(event.getAuthor()))
            handleMessage(event);
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

        if (Config.developersOnly)
            return Config.isDeveloper(user);

        return true;
    }

    private void handleMessage(MessageReceivedEvent messageReceivedEvent) {
        MessageEvent event = new MessageEvent(chatbot, messageReceivedEvent);

        if (!event.isValid())
            return;

        log(Level.INFO, event.getMessage().getContentRaw());

        CommandHandler handler = getHandler(event);

        if (handler == null) {
            log(Level.INFO, "Command was attemped in unregistered module {0}.", event.getModule());
            return;
        }

        if (handler.getClass().getAnnotation(Developer.class) != null) {
            if (!Config.isDeveloper(event.getAuthor()))
                return;
        }

        if (event.getCommand() == null)
            event.setCommand(handler);

        Collection<Method> commands = CommandUtils.getCommands(event, handler);

        if (commands.isEmpty()) {
            event.reply("Sorry, that command doesn't exist!");
            return;
        }

        Method method = CommandUtils.getByParamCount(event, commands);

        if (method == null) {
            event.reply("Those parameters don't look right. DX Try help?");
            return;
        }

        Permissions permissions = method.getAnnotation(Permissions.class);

        if (permissions != null) {
            if (!event.getMember().hasPermission(permissions.value())) {
                event.reply("You lack permission to do this command.");
                return;
            }
        }

        try {
            Object[] params = CommandUtils.parseParameters(event, method);

            try {
                method.invoke(handler, params);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
                BotUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
            }
        } catch (IllegalArgumentException ex) {
            event.reply(ex.getMessage());
        }
    }

    private CommandHandler getHandler(MessageEvent event) {
        String module = event.getModule();

        for (Collection<String> key : handlers.keySet()) {
            if (key.contains(module))
                return handlers.get(key);
        }

        return null;
    }
}
