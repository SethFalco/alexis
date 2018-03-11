package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.events.CommandEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import net.dv8tion.jda.core.entities.ChannelType;

@Module (
    aliases = {"greetings", "settings", "setting", "conf"},
    help = "Configure some of the settings and tailor Alexis for your guild!",
    scope = ChannelType.TEXT,
    requiresDatabase = true
)
public class GreetingsHandler extends CommandHandler {

    @Command (
        aliases = "welcome",
        help = "Enable or disable the welcome "
    )
    public void setWelcomeMessage(CommandEvent event) {

    }
}
