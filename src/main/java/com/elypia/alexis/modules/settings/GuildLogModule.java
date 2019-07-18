package com.elypia.alexis.modules.settings;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

@Module(id = "Guild Logging", group = "Settings", aliases = {"log", "logging", "logger"}, help = "guild_logging.help")
public class GuildLogModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public GuildLogModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }
}
