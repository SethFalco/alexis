package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.Brainfuck;

@Module(
    aliases = {"Brainfuck", "bf"},
    help = "Compile Brainfuck code into Strings."
)
public class BrainfuckHandler extends CommandHandler {

    @Command (
        aliases = {"compile", "interpret"},
        help = "Compile Brainfuck code into something non-nerds understand.",
        params = {
            @Parameter(
                param = "code",
                help = "The code to compile.",
                type = String.class
            )
        }
    )
    public void interpretBrainfuck(MessageEvent event) {
        Object[] params = event.getParams();
        String code = (String)params[0];

        try {
            Brainfuck brainfuck = Brainfuck.compile(code);
            String result = brainfuck.interpret();
            event.reply(result);
        } catch (IllegalArgumentException ex) {
            event.reply(ex.getMessage());
        }
    }
}
