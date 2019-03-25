package com.elypia.alexis.commandler.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Nsfw;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(id = "Miscellaneous", aliases = {"util", "utils"}, help = "misc.h")
public class MiscModule extends JDACHandler {

    private OkHttpClient client;
    private Request nekoRequest;
    private List<String> nekoCache;

    public MiscModule() {
        client = new OkHttpClient();
        nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
        nekoCache = new ArrayList<>();
    }

//    @Static
//	@Command(id = "misc.convert", aliases = {"convert"}, help = "misc.convert.h")
//	@Param(id = "common.value", help = "misc.convert.p.value.h")
//	public EmbedBuilder asWritten(JDACEvent event, long values[]) {
//		EmbedBuilder builder = new EmbedBuilder();
//		builder.setTitle("**" + scripts.get("misc.convert.results") + "**");
//
//		for (long l : values) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("**  " + scripts.get("misc.convert.input") + "**\n```");
//			sb.append(l);
//			sb.append("```\n** " + scripts.get("misc.convert.output") + "**\n```");
//
//			String result = MathUtils.asWritten(l);
//			sb.append(result == null ? scripts.get("misc.convert.error") : result);
//
//			sb.append("```");
//
//			builder.addField("", sb.toString(), false);
//		}
//
//		return builder;
//	}

    @Static
    @Command(id = "c.count", aliases = "count", help = "ch.count")
    public String count(JDACEvent event,
        @Param(id = "p.body", help = "ph.body") String input
    ) {
        var params = Map.of("length", input.length());
        return scripts.get("misc.count.response", params);
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
