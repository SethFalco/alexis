package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.jda.JDAHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.elypiai.brainfuck.Brainfuck;

@Module(name = "Brainfuck", aliases = {"brainfuck", "bf"}, help = "help.brainfuck")
public class BrainfuckHandler extends JDAHandler {

    @Default
    @Command(id = 6, name = "Interpret Code", aliases = {"compile", "interpret"}, help = "help.brainfuck.compile")
    @Param(name = "code", help = "help.brainfuck.compile.code")
    public String interpretBrainfuck(String code) {
        return interpretBrainfuck(code, new byte[0]);
    }

    @Overload(6)
    @Param(name = "args", help = "help.brainfuck.compile.args")
    public String interpretBrainfuck(String code, byte[] args) {
        try {
            Brainfuck brainfuck = Brainfuck.compile(code, args);
            return brainfuck.interpret();
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }
}
