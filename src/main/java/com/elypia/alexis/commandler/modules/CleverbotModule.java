package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.validation.Database;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.elypiai.cleverbot.Cleverbot;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

@Module(id = "Cleverbot", aliases = {"cleverbot", "cb"}, help = "cb.help")
public class CleverbotModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(CleverbotModule.class);

    private Cleverbot cleverbot;

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public CleverbotModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
        cleverbot = new Cleverbot(Alexis.config.getApiCredentials().getCleverbot());
    }

    @Command(id = "cb.say.name", aliases = {"say", "ask"}, help = "cb.say.help")
    @Param(id = "common.body", help = "cb.param.body.help")
    public void say(@Database JDACEvent event, String body) {
        MessageChannel channel = event.getSource().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();

        cleverbot.say(body, cs).queue(response -> {
            event.send(response.getOutput());

            data.setCleverState(response.getCs());
            data.commit();
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }

//    @Command(name = "Channel Chat History", aliases = {"history", "his"}, help = "cb.history.help")
//    public String getHistory(JDACommand event) {
//        MessageChannel channel = event.getMessageEvent().getChannel();
//        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
//        String cs = data.getCleverState();
//        String history = cleverbot.getHistory(cs);
//
//        if (history == null)
//            return "BotUtils.getScript("cb.no_history", event.getSource())";
//        else
//            return String.format("```\n%s\n```", history);
//    }
}
