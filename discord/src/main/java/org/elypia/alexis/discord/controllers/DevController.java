/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.AlexisExitCode;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.Scope;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.BotOwner;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class DevController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(DevController.class);

    private final AlexisMessages messages;

    @Inject
    public DevController(AlexisMessages messages) {
        this.messages = messages;
    }

    @StandardCommand
    public String guilds(@BotOwner ActionEvent<Event, Message> event) {
        StringJoiner joiner = new StringJoiner("\n");
        Collection<Guild> guilds = event.getRequest().getSource().getJDA().getGuilds();

        for (Guild guild : guilds)
           joiner.add(messages.devGuildInfo(guild.getId(), guild.getName(), guild.getMembers().size()));

        return joiner.toString();
    }

    @StandardCommand
    public String leave(@BotOwner ActionEvent<Event, Message> event, @Param @Scoped(inGuild = Scope.LOCAL, inPrivate = Scope.GLOBAL) Guild guild) {
        guild.leave().queue();
        return messages.devLeftGuild();
    }

    @StandardCommand
    public String rename(@BotOwner ActionEvent<Event, Message> event, @Param String input) {
        event.getRequest().getSource().getJDA().getSelfUser().getManager().setName(input).complete();
        return messages.devChangedBotsName(input);
    }

    @StandardCommand
    public String avatar(@BotOwner ActionEvent<Event, Message> event, @Param URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);
            event.getRequest().getSource().getJDA().getSelfUser().getManager().setAvatar(icon).complete();
            return messages.devChangedBotsAvatar();
        }
    }

    @StandardCommand
    public void shutdown(@BotOwner ActionEvent<Event, Message> event) {
        event.getRequest().getSource().getJDA().shutdown();
        logger.info("Logging out of Discord.");
        System.exit(AlexisExitCode.NORMAL.getExitCode());
    }

    /**
     * @param event
     * @return
     */
    @StandardCommand
    public String clean(@BotOwner ActionEvent<Event, Message> event) {
        event.getRequest().getSource().getJDA().getGuilds().forEach((guild) -> {
            //
        });

        return messages.done();
    }
}
