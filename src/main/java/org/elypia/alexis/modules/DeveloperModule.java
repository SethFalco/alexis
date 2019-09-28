/*
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

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Icon;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.entities.embedded.AssignableRole;
import org.elypia.commandler.annotations.*;
import org.elypia.jdac.*;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.Developer;
import org.slf4j.*;

import java.io.*;
import java.net.URL;
import java.util.*;

@Module(id = "Developer", aliases = "dev")
public class DeveloperModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperModule.class);

    @Command(id = "dev.rename", aliases = "name", help = "dev.name.h")
    public void name(
        @Developer JDACEvent event,
        @Param(id = "dev.name.p.name", help = "dev.name.p.name.h") String input
    ) {
        event.getSource().getJDA().getSelfUser().getManager().setName(input).queue(o -> {
            var params = Map.of("name", input);
            event.send("dev.name.success");
        }, failure -> event.send("dev.name.failure"));
    }

    @Command(id = "dev.avatar", aliases = {"avatar", "pp"}, help = "dev.avatar.h")
    public void avatar(
        @Developer JDACEvent event,
        @Param(id = "common.url", help = "dev.avatar.p.url.h") URL url
    ) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            event.getSource().getJDA().getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.send("dev.avatar.success");
            }, failure -> event.send("dev.name.failure"));
        }
    }

    public void shutdownCommand(@Developer JDACEvent event) {
        JDA jda = event.getSource().getJDA();
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdown();

        ExitStatus code = ExitStatus.PEACEFUL;

        logger.trace(code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(id = "dev.embed", aliases = "embed", help = "dev.embed.h")
    public EmbedBuilder embedTest(
        @Developer JDACEvent event,
        @Param(id = "common.url", help = "dev.embed.p.url.h") String url
    ) {
        return new EmbedBuilder().setImage(url);
    }

    @Command(id = "dev.clean", aliases = "clean")
    public String clean(@Developer JDACEvent event) {
        event.getSource().getJDA().getGuilds().forEach((guild) -> {
            GuildData data = GuildData.query(guild);
            List<AssignableRole> roles = data.getSettings().getAssignableRoles();
            int originalSize = roles.size();

            for (int i = roles.size() - 1; i >= 0; i--) {
                if (guild.getRoleById(roles.get(i).getRoleId()) == null)
                    roles.remove(i);
            }

            if (originalSize != roles.size())
                data.commit();
        });

        return "Done!";
    }
}
