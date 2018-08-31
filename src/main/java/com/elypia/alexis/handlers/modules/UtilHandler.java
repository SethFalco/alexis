package com.elypia.alexis.handlers.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Nsfw;
import com.elypia.elypiai.utils.math.MathUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(name = "Miscellaneous", aliases = {"util", "utils"}, help = "Miscellaneous commands that don't fit into another module. All of these are static.")
public class UtilHandler extends JDAHandler {

    private OkHttpClient client;
    private Request nekoRequest;
    private List<String> nekoCache;

    public UtilHandler() {
        client = new OkHttpClient();
        nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
        nekoCache = new ArrayList<>();
    }

    @Static
	@Command(name = "Number as Written", aliases = {"convert"}, help = "Convert a number to it's written equivelent.")
	@Param(name = "value", help = "The number to convert to the written form.")
	public EmbedBuilder asWritten(long values[]) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("**Results**");

		for (long l : values) {
			StringBuilder sb = new StringBuilder();
			sb.append("**  Input**\n```");
			sb.append(l);
			sb.append("```\n** Output**\n```");

			String result = MathUtils.asWritten(l);
			sb.append(result == null ? "Error" : result);

			sb.append("```");

			builder.addField("", sb.toString(), false);
		}

		return builder;
	}

	@Static
	@Command(name = "Character Counter", aliases = "count", help = "Cound the number of characters in the given parameter.")
	@Param(name = "text", help = "The text to count letter by letter.")
	public String count(String input) {
		return String.format("There are %,d characters.", input.length());
	}

    @Static
    @Nsfw
    @Command(name = "Get Pet Neko", aliases = "neko", help = "Get a pet neko sent to you over Discord.")
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
                    event.reply("We're all out of nekos. :C Maybe try again later?");
                    return;
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setImage(nekoCache.get(ThreadLocalRandom.current().nextInt(nekoCache.size())));
                event.reply(builder);
            }
        });
    }
}
