package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.CommandEvent;
import com.elypia.alexis.discord.handlers.commands.CommandHandler;
import com.elypia.elypiai.Brainfuck;

@Module(
    aliases = {"Brainfuck", "bf"},
    help = "Compile Brainfuck code into Strings."
)
public class BrainfuckHandler extends CommandHandler {

    private Brainfuck brainfuck;

    public BrainfuckHandler() {
        brainfuck = new Brainfuck();
    }

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
    public void interpretBrainfuck(CommandEvent event) {
        Object[] params = event.getParams();
        String code = (String)params[0];
        String result = new Brainfuck().compileToString(code);

        event.reply(result);
    }
}
