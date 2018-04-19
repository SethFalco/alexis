package com.elypia.alexis.discord;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.discord.audio.controllers.LocalAudioController;
import com.elypia.alexis.discord.handlers.modules.*;
import com.elypia.alexis.discord.managers.EventManager;
import com.elypia.commandler.jda.JDACommandler;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.json.*;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Chatbot {

	private JDA jda;

	private List<Game> statuses;

	public Chatbot() throws LoginException {
	    statuses = new ArrayList<>();

	    JSONObject discord = Alexis.config.getJSONObject("discord");

		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(discord.getString("token"));
		builder.setCorePoolSize(10);
		builder.setStatus(OnlineStatus.IDLE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.addEventListener(new EventManager(this));

        JSONArray statusesArray = discord.getJSONArray("statuses");

        for (int i = 0; i < statusesArray.length(); i++) {
            JSONObject status = statusesArray.getJSONObject(i);
            int key = status.getInt("key");
            String text = status.getString("text");

            statuses.add(Game.of(Game.GameType.fromKey(key), text));
        }

        builder.setGame(statuses.get(0));

		jda = builder.buildAsync();

        JSONObject apiDetails = Alexis.config.getJSONObject("api_details");
        JDACommandler commandler = new JDACommandler(jda);

        commandler.registerModules(
            new AmazonHandler(apiDetails.getJSONObject("amazon")),
            new BotHandler(),
            new BrainfuckHandler(),
            new CleverbotHandler(apiDetails.getJSONObject("cleverbot").getString("api_key")),
            new DevHandler(),
            new EmoteHandler(),
            new GuildHandler(),
            new UtilHandler(),
            new MusicHandler(LocalAudioController.class),
            new MyAnimeListHandler(apiDetails.getJSONObject("myanimelist").getString("token")),
            new NanowrimoHandler(),
            new OsuHandler(apiDetails.getJSONObject("osu").getString("api_key")),
            new RuneScapeHandler(),
            new SteamHandler(apiDetails.getJSONObject("steam").getString("api_key")),
            new TwitchHandler(apiDetails.getJSONObject("twitch").getString("api_key")),
            new UrbanDictionaryHandler(),
            new UserHandler(),
            new VoiceHandler(),
            new YouTubeHandler(apiDetails.getJSONObject("google").getString("api_key"))
        );
	}

	public JDA getJDA() {
		return jda;
	}
}
