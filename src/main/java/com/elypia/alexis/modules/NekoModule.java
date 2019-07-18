package com.elypia.alexis.modules;

import com.elypia.cmdlrdiscord.constraints.Nsfw;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import net.dv8tion.jda.api.*;
import okhttp3.*;
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
