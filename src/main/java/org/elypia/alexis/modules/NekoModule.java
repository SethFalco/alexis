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
import okhttp3.*;
import org.elypia.comcord.constraints.Nsfw;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(name = "Neko", aliases = {"neko"}, help = "neko bois")
public class NekoModule {

    private OkHttpClient client;
    private Request nekoRequest;
    private List<String> nekoCache;

    public NekoModule() {
        client = new OkHttpClient();
        nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
        nekoCache = new ArrayList<>();
    }

    @Default
    @Command(name = "Pet Neko", aliases = "pet", help = "pet a neko")
    public void neko(@Nsfw CommandlerEvent<?, ?> event) {
        client.newCall(nekoRequest).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object = new JSONObject(response.body().string());

                EmbedBuilder builder = new EmbedBuilder();
                String image = object.getString(line);
                builder.setImage(image);
                new MessageBuilder(builder.build()).build();

                if (!nekoCache.contains(image))
                    nekoCache.add(image);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (nekoCache.size() == 0) {
                    event.send("No neko for you, fuck you.");
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setImage(nekoCache.get(ThreadLocalRandom.current().nextInt(nekoCache.size())));
                event.send(builder);
            }
        });
    }
}
