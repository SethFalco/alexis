package com.elypia.alexis.commandler.modules.settings;

import com.elypia.commandler.annotations.Module;
import com.elypia.jdac.alias.JDACHandler;

@Module(id = "Guild Logging", group = "Settings", aliases = {"log", "logging", "logger"}, help = "guild_logging.help")
public class GuildLogModule extends JDACHandler {

}
