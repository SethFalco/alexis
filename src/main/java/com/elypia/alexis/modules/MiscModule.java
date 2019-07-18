package com.elypia.alexis.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Nsfw;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
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
