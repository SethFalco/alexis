package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Module;
import net.dv8tion.jda.core.entities.ChannelType;

@Module (
    aliases = {"config", "settings", "setting", "conf"},
    help = "Configure some of the settings and tailor Alexis for your guild!",
    scope = ChannelType.TEXT,
    requiresDatabase = true
)
public class ConfigHandler {

    public void
}
