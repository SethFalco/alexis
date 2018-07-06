package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.brainfuck.Brainfuck;

@Module(name = "Brainfuck", aliases = {"brainfuck", "bf"}, description = "Compile Brainfuck code into Strings.")
public class BrainfuckHandler extends CommandHandler {

    @Default
    @Command(id = 6, name = "Interpret Code", aliases = {"compile", "interpret"}, help = "Compile Brainfuck code into something non-nerds understand.")
    @Param(name = "code", help = "The code to compile.")
    public String interpretBrainfuck(String code) {
        return interpretBrainfuck(code, new byte[0]);
    }

    @Overload(6)
    @Param(name = "code", help = "The code to compile.")
    @Param(name = "args", help = "The arguments for input, represented by a , in brainfuck.")
    public String interpretBrainfuck(String code, byte[] args) {
        try {
            Brainfuck brainfuck = Brainfuck.compile(code, args);
            return brainfuck.interpret();
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }
}
