package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Database;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Scope;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import net.dv8tion.jda.core.entities.ChannelType;

@Module (
    aliases = {"greetings", "settings", "setting", "conf"},
    help = "Configure some of the settings and tailor Alexis for your guild!"
)
@Scope(ChannelType.TEXT)
@Database
public class GreetingsHandler extends CommandHandler {

    @Command(aliases = "welcome", help = "Enable or disable the welcome.")
    public void setWelcomeMessage(MessageEvent event) {

    }
}
