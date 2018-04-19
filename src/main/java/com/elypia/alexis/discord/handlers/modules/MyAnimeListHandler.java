package com.elypia.alexis.discord.handlers.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.command.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.elypiai.myanimelist.MyAnimeList;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import static com.elypia.alexis.utils.BotUtils.httpFailure;

@Module(aliases = {"myanimelist", "mal"}, help = "Search through MyAnimeList for animes and managas.")
public class MyAnimeListHandler extends CommandHandler {

    private MyAnimeList mal;

    public MyAnimeListHandler(String auth) {
        mal = new MyAnimeList(auth);
    }

    public MyAnimeListHandler(String user, String password) {
        mal = new MyAnimeList(user, password);
    }

    @Command(aliases = "anime", help = "Get information on an anime.")
    @Param(name = "animes", help = "A search terms to find shows.")
    public void getAnime(MessageEvent event, String anime) {
        mal.getAnime(anime, result -> {
            EmbedBuilder builder = new EmbedBuilder();

            if (result == null) {
                event.reply("Sorry, I didn't find the show you were looking for.");
                return;
            }

            int score = (int)result.getScore();
            StringBuilder rating = new StringBuilder();

            for (int i = 0; i < score; i++)
                rating.append("\\⭐");

            builder.setTitle(result.getTitle() + " " + rating.toString());
            builder.setImage(result.getImageUrl());

            builder.addField("Type", result.getType().getApiName(), true);
            builder.addField("Status", result.getStatus(), true);

            int episodeCount = result.getEpisodes();
            String episodes = episodeCount == 0 ? "Ongoing" : String.valueOf(episodeCount);
            builder.addField("Episodes", episodes, true);

            String endDate = result.getEndDate();
            String airing = result.getStartDate() + " - " + (endDate != null ? endDate : "Ongoing");
            builder.addField("Airing", airing, true);

            String syn = result.getSynopsis();

            if (syn.length() >= MessageEmbed.VALUE_MAX_LENGTH)
                syn = syn.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";

            builder.addField("Synposis", syn, false);

            event.reply(builder);
        }, failure -> httpFailure(event, failure));
    }

    public void getManga(MessageEvent event, String[] mangas) {

    }
}
