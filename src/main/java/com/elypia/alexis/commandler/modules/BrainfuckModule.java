package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.jdac.alias.JDACHandler;

@Module(id = "Brainfuck", aliases = {"brainfuck", "bf"}, help = "brainfuck.help")
public class BrainfuckModule extends JDACHandler {

//    @Default
//    @Command(id = "brainfuck.compile.name", aliases = {"compile", "interpret"}, help = "brainfuck.help")
//    @Param(id = "code", help = "brainfuck.param.code.help")
//    public String interpretBrainfuck(String code) {
//        return interpretBrainfuck(code, new byte[0]);
//    }
//
//    @Overload("brainfuck.compile.name")
//    @Param(id = "brainfuck.param.args.name", help = "brainfuck.param.args.help")
//    public String interpretBrainfuck(String code, byte[] args) {
//        try {
//            Brainfuck brainfuck = Brainfuck.compile(code, args);
//            return brainfuck.interpret();
//        } catch (IllegalArgumentException ex) {
//            return ex.getMessage();
//        }
//    }
}
