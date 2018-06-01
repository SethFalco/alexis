package com.elypia.alexis.discord.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.Reaction;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.elypiai.steam.Steam;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.EmbedBuilder;

@Module(
	name = "Steam",
	aliases = "steam",
	description = "Integration with the popular DRM, Steam!"
)
public class SteamHandler extends CommandHandler {

	private Steam steam;

	public SteamHandler(String apiKey) {
		steam = new Steam(apiKey);
	}

	@Command(aliases = {"get", "user", "profile"}, help = "Get information on a Steam user!")
	@Param(name = "username", help = "The name you'd find at the end of their custom URL!")
	public void displayProfile(MessageEvent event, String username) {
		steam.getUser(username, user -> {
			if (user == null) {
				event.reply("I don't think that user exists?");
				return;
			}

			EmbedBuilder builder = new EmbedBuilder();

			builder.setTitle(user.getUsername(), user.getProfileURL());
			builder.setThumbnail(user.getAvatar());

			event.reply(builder);
		}, failure -> BotUtils.sendHttpError(event, failure));
	}

	@Command(aliases = {"lib", "library"}, help = "Get a players library orderd by recent playtime, then playtime!")
	@Param(name = "username", help = "The username or ID of the user!")
	public void listLibrary(MessageEvent event, String username) {
		steam.getUser(username, user -> {
			user.getLibrary(library -> {
				if (user == null) {
					event.reply("I don't think that user exists?");
					return;
				}

				Object[][] games = new Object[library.size() + 1][3];

				games[0][0] = "Title";
				games[0][1] = "Total Playtime";
				games[0][2] = "Recent Playtime";

				for (int i = 1; i < library.size(); i++) {
					SteamGame game = library.get(i);

					games[i][0] = game.getName();
					games[i][1] = game.getTotalPlaytime();
					games[i][2] = game.getRecentPlaytime();
				}

//				String message = ElyUtils.generateTable(1992, games);
//				event.reply(String.format("```\n%s\n```", message));
			}, failure -> BotUtils.sendHttpError(event, failure));
		}, failure -> BotUtils.sendHttpError(event, failure));
	}

	@Command(aliases = {"random", "rand", "game", "r"}, help = "Select a random game from the players library!")
	@Param(name = "username", help = "The username or ID of the user!")
	@Reaction(alias = "🎲", help = "Reroll for a different game.")
	public void randomGame(MessageEvent event, String username) {
		steam.getUser(username, user -> {
			if (user == null) {
				event.reply("I don't think that user exists?");
				return;
			}

			user.getLibrary(library -> {
				SteamGame game = library.get(ElyUtils.RANDOM.nextInt(library.size()));

				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle(game.getName(), game.getGameUrl());
				builder.setThumbnail(game.getIconUrl());

				String totalPlaytime = String.format("%,d", game.getTotalPlaytime());
				builder.addField("Total Playtime", totalPlaytime, true);

				String recentPlaytime = String.format("%,d", game.getRecentPlaytime());
				builder.addField("Recent Playtime", recentPlaytime, true);

				builder.setImage(game.getLogoUrl());
				event.reply(builder);
			}, failure -> BotUtils.sendHttpError(event, failure));
		}, failure -> BotUtils.sendHttpError(event, failure));
	}
}
