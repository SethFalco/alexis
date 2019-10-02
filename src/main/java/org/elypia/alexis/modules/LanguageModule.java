/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.metadata.ModuleData;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.Channels;

import java.util.*;

@Module(id = "Language", group = "Settings", aliases = {"language", "languages", "lang"}, help = "lang.help")
public class LanguageModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public LanguageModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "lang.global.title", aliases = "global", help = "lang.global.help")
    @Param(id = "common.lang", help = "lang.param.lang.help")
    public String setGlobalLanguage(@Channels(ChannelType.TEXT) JDACEvent event, @Supported Language language) {
        List<TextChannel> channels = event.asMessageRecieved().getGuild().getTextChannels();
        setLanguages(language, channels.toArray(new MessageChannel[0]));

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , false
        );

        return scripts.get(event.getSource(), "global.lang.changed", params);
    }

    @Command(id = "lang.local.title", aliases = "local", help = "lang.local.help")
    @Param(id = "common.lang", help = "lang.param.lang.help")
    public String setLocalLanguage(JDACEvent event, @Supported Language language) {
        MessageChannel channel = event.asMessageRecieved().getChannel();
        setLanguages(language, channel);

        boolean isPrivate = !channel.getType().isGuild();

        Map<String, Object> params = Map.of(
            "language", language.getName(),
            "private" , isPrivate
        );

        return scripts.get("local.lang.changed", params);
    }

    private void setLanguages(Language language, MessageChannel... channels) {
        String code = language.getCode();

        for (MessageChannel channel : channels) {
            long id = channel.getIdLong();

            MessageChannelData data = MessageChannelData.query(id);
            data.setLanguage(code);
            data.commit();
        }
    }
}
