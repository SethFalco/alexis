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
import okhttp3.*;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.Handler;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.Nsfw;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(name = "Miscellaneous", aliases = {"util", "utils"}, help = "misc.h")
public class MiscModule implements Handler {

    private OkHttpClient client;
    private Request nekoRequest;
    private List<String> nekoCache;

    public MiscModule() {
        client = new OkHttpClient();
        nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
        nekoCache = new ArrayList<>();
    }

    @Static
    @Command(name = "c.count", aliases = "count", help = "ch.count")
    public String count(
        @Param(name = "body", help = "ph.body") String input
    ) {
        var params = Map.of("length", input.length());
        return scripts.get("misc.count.resp", params);
    }

    @Static
    @Command(id = "misc.neko", aliases = "neko", help = "misc.neko.h")
    public void neko(@Nsfw JDACEvent event) {
        client.newCall(nekoRequest).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object = new JSONObject(response.body().string());

                EmbedBuilder builder = new EmbedBuilder();
                String image = object.getString("neko");
                builder.setImage(image);
                event.send(builder);

                if (!nekoCache.contains(image))
                    nekoCache.add(image);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (nekoCache.size() == 0) {
                    event.send("misc.neko.failure");
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setImage(nekoCache.get(ThreadLocalRandom.current().nextInt(nekoCache.size())));
                event.send(builder);
            }
        });
    }
}
