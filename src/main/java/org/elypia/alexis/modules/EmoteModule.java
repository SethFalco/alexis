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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.*;

import javax.inject.*;
import java.util.*;

@Singleton
@Module(name = "emote", group = "discord", aliases = {"emote", "emoji", "emoticon"})
public class EmoteModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public EmoteModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "emote.list", aliases = "emote.list.h")
    public String list(CommandlerEvent<Event, Message> event, @Param(name = "p.guild", defaultValue = "${source.guild}") Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        PropertyResourceBundle.getBundle("AlexisMessages", Locale.ENGLISH);

        if (count == 0)
            return lang.get(event, "emote.no_emotes");

        int length = (int)(Math.sqrt(count) + 1) * 2;

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            if (i % length == 0)
                builder.append("\n");

            builder.append(emotes.get(i).getAsMention());
        }

        return builder.toString();
    }

    @Command(name = "send", aliases = {"get", "post"})
    public EmbedBuilder post(CommandlerEvent<Event, Message> event, @Param(name = "emote") Emote emote) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setImage(emote.getImageUrl());
        return builder;
    }
}
