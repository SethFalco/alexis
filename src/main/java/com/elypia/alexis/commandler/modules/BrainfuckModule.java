package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

@Module(id = "Brainfuck", aliases = {"brainfuck", "bf"}, help = "brainfuck.help")
public class BrainfuckModule extends JDACHandler {
    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public BrainfuckModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

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
