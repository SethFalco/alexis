package com.elypia.alexis.handlers.modules.settings;

import com.elypia.commandler.jda.JDAHandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import net.dv8tion.jda.core.entities.ChannelType;

@Channel(ChannelType.TEXT)
@Module(name = "guild_logging.title", group = "Settings", aliases = {"log", "logging", "logger"}, help = "guild_logging.help")
public class GuildLogHandler extends JDAHandler {

}
