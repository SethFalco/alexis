package com.elypia.alexis.discord.managers;

import com.elypia.alexis.discord.Chatbot;
import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.discord.managers.impl.DiscordManager;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;

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
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (config.developersOnly() && !config.isDeveloper(event.getAuthor()))
            return;

        if (event.getAuthor().isBot())
            return;

        MessageEvent e = new MessageEvent(chatbot, event);

        chatbot.getGlobalMessageHandler().handleXp(e);

        handleMessage(e);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

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
            BotUtils.LOGGER.log(Level.SEVERE, "{0} doesn't include the @Module annotation!", handler);
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

    public void handleMessage(MessageEvent event) {
        if (!event.isValid())
            return;

        BotUtils.LOGGER.log(Level.INFO, event.getMessage().getContentRaw());

        String commandModule = event.getModule();
        CommandHandler handler = null;

        for (String[] aliases : handlers.keySet()) {
            if (ElyUtils.arrayContains(commandModule, aliases)) {
                handler = handlers.get(aliases);
                break;
            }
        }

        if (handler == null) {
            BotUtils.LOGGER.log(Level.FINEST, "A user attmped to access the module {0}, which doesn't exist!", commandModule);
            return;
        }

        Method[] methods = handler.getClass().getMethods();
        Method commandMethod = null;
        Command command = null;

        for (Method method : methods) {
            command = method.getAnnotation(Command.class);

            if (command == null)
                continue;

            if (ElyUtils.arrayContains(event.getCommand(), command.aliases()))
                commandMethod = method;
        }

        if (commandMethod == null) {
            event.getChannel().sendMessage("That command doens't exist, try help?").queue();
            return;
        }

        event.setMethod(commandMethod);

        try {
            commandMethod.invoke(handler, event);
            event.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
            BotUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
        }
    }
}
