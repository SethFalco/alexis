package com.elypia.alexis.discord.handlers.commands.modules;


import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.CommandGroup;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.elypia.alexis.utils.BotUtils.httpFailure;

@Module(
    aliases = {"osu", "osu!"},
    help = "Integration with osu!, the popular rhythem game."
)
public class OsuHandler extends CommandHandler {

    private Osu osu;

    public OsuHandler(String apikey) {
        osu = new Osu(apikey);
    }

    @CommandGroup("get")
    @Command(aliases = "get", help = "Get stats on osu! players.")
    @Parameter(name = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(MessageEvent event, String[] players) throws InterruptedException {
        getPlayers(event, players, OsuMode.OSU);
    }

    @CommandGroup("get")
    @Parameter(name = "players", help = "The players usernames you want to retrieve.")
    @Parameter(name = "mode", help = "The mode to use when getting players.")
    public void getPlayers(MessageEvent event, String[] players, OsuMode mode) throws InterruptedException {
        if (players.length == 1)
            getSinglePlayer(event, players[0], mode);
        else
            getMultiplePlayers(event, players, mode);
    }

    private void getSinglePlayer(MessageEvent event, String username, OsuMode mode) {
        EmbedBuilder builder = new EmbedBuilder();

        osu.getUser(username, mode, result -> {
            if (result == null) {
                event.reply("Sorry, I couldn't find the user you specified.");
                return;
            }

            builder.setThumbnail(result.getAvatarUrl());
            builder.addField("Username", Markdown.a(result.getUsername(), result.getProfileUrl()), true);
            builder.addField("Level", String.valueOf((int)result.getLevel()), true);
            builder.addField("Ranked Score", result.getRankedScoreString(), true);
            builder.addField("Total Score", result.getTotalScoreString(), true);
            builder.addField("PP", result.getPpString(), true);
            builder.addField("Rank", String.valueOf(result.getRank()), true);
            builder.addField("Accuracy", result.getAccuracyString(), true);
            builder.addField("Play count", String.format("%,d", result.getPlayCount()), true);
            event.reply(builder);
        }, failure -> httpFailure(event, failure));
    }

    private void getMultiplePlayers(MessageEvent event, String[] players, OsuMode mode) throws InterruptedException {
        int length = players.length;
        EmbedBuilder builder = new EmbedBuilder();
        CountDownLatch latch = new CountDownLatch(length);

        for (String s : players) {
            osu.getUser(s, mode, result -> {
                if (result != null) {
                    StringJoiner joiner = new StringJoiner("\n");
                    joiner.add("**Level**: " + (int)result.getLevel());
                    joiner.add("**Ranked Score**: " + result.getRankedScoreString());
                    joiner.add("**PP**: " + result.getPpString());
                    joiner.add("**Accuracy**: " + result.getAccuracyString());

                    builder.addField(result.getUsername(), joiner.toString(), false);
                }

                latch.countDown();
            }, failure -> {
                httpFailure(event, failure);
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);

        if (builder.isEmpty()) {
            event.reply("Sorry, I couldn't find any of the users you specified.");
            return;
        }

        if (builder.length() != players.length)
            builder.setFooter("Sorry, I couldn't find all the players but I returned what I could.", null);

        event.reply(builder);
    }
}
