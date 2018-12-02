package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.JDAHandler;
import com.elypia.elypiai.brainfuck.Brainfuck;

@Module(name = "brainfuck.title", aliases = {"brainfuck", "bf"}, help = "brainfuck.help")
public class BrainfuckHandler extends JDAHandler {

    @Default
    @Command(id = 6, name = "brainfuck.compile.name", aliases = {"compile", "interpret"}, help = "brainfuck.help")
    @Param(name = "code", help = "brainfuck.param.code.help")
    public String interpretBrainfuck(String code) {
        return interpretBrainfuck(code, new byte[0]);
    }

    @Overload(6)
    @Param(name = "brainfuck.param.args.name", help = "brainfuck.param.args.help")
    public String interpretBrainfuck(String code, byte[] args) {
        try {
            Brainfuck brainfuck = Brainfuck.compile(code, args);
            return brainfuck.interpret();
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }
}
