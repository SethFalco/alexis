package com.elypia.alexis.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Nsfw;
import com.elypia.elypiai.utils.math.MathUtils;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(name = "misc", aliases = {"util", "utils"}, help = "misc.h")
public class MiscModule extends JDACHandler {

    private OkHttpClient client;
    private Request nekoRequest;
    private List<String> nekoCache;

    public MiscModule() {
        client = new OkHttpClient();
        nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
        nekoCache = new ArrayList<>();
    }

    @Static
	@Command(name = "misc.convert", aliases = {"convert"}, help = "misc.convert.h")
	@Param(name = "common.value", help = "misc.convert.p.value.h")
	public EmbedBuilder asWritten(JDACommand event, long values[]) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**" + event.getScript("misc.convert.results") + "**");

		for (long l : values) {
			StringBuilder sb = new StringBuilder();
			sb.append("**  " + event.getScript("misc.convert.input") + "**\n```");
			sb.append(l);
			sb.append("```\n** " + event.getScript("misc.convert.output") + "**\n```");

			String result = MathUtils.asWritten(l);
			sb.append(result == null ? event.getScript("misc.convert.error") : result);

			sb.append("```");

			builder.addField("", sb.toString(), false);
		}

		return builder;
	}

	@Static
	@Command(name = "misc.count", aliases = "count", help = "misc.count.h")
	@Param(name = "common.body", help = "misc.count.p.body.h")
	public String count(JDACommand event, String input) {
        var params = Map.of("length", input.length());
        return event.getScript("misc.count.response", params);
	}

    @Static
    @Nsfw
    @Command(name = "misc.neko", aliases = "neko", help = "misc.neko.h")
    public void neko(JDACommand event) {
        client.newCall(nekoRequest).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject object = new JSONObject(response.body().string());

                EmbedBuilder builder = new EmbedBuilder();
                String image = object.getString("neko");
                builder.setImage(image);
                event.reply(builder);

                if (!nekoCache.contains(image))
                    nekoCache.add(image);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (nekoCache.size() == 0) {
                    event.replyScript("misc.neko.failure");
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setImage(nekoCache.get(ThreadLocalRandom.current().nextInt(nekoCache.size())));
                event.reply(builder);
            }
        });
    }
}
