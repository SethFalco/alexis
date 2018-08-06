package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.Emoji;
import com.elypia.elypiai.steam.*;

import java.util.concurrent.ThreadLocalRandom;

@Module(name = "Steam", aliases = "steam", help = "Integration with the popular DRM, Steam!")
public class SteamHandler extends JDAHandler {

	private Steam steam;

	public SteamHandler(String apiKey) {
		steam = new Steam(apiKey);
	}

	@Command(name = "Steam Profile", aliases = {"get", "user", "profile"}, help = "Get information on a Steam user!")
	@Param(name = "username", help = "The name you'd find at the end of their custom URL!")
	public void displayProfile(JDACommand event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.reply("Sorry, I couldn't find that user.");
				return;
			}

			steam.getUsers(search.getId()).queue(users -> {
				SteamUser user = users.get(0);
				event.reply(user);
			}, failure -> BotLogger.log(event, failure));
		});
	}

	@Command(name = "Steam Library", aliases = {"lib", "library"}, help = "Get a players library orderd by recent playtime, then playtime!")
	@Param(name = "username", help = "The username or ID of the user!")
	public void listLibrary(JDACommand event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.reply("Sorry, I couldn't find that user.");
				return;
			}

			steam.getLibrary(search.getId()).queue(library -> {
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
			}, failure -> BotLogger.log(event, failure));
		});
	}

	@Command(id = 3, name = "Game Roulette", aliases = {"random", "rand", "game", "r"}, help = "Select a random game from the players library!")
	@Param(name = "username", help = "The username or ID of the user!")
	@Emoji(emotes = "🎲", help = "Reroll for a different game.")
	public void randomGame(JDACommand event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.reply("Sorry, I couldn't find that user.");
				return;
			}

			steam.getLibrary(search.getId()).queue(library -> {
//				event.storeObject("library", library);

				SteamGame game = library.get(ThreadLocalRandom.current().nextInt(library.size()));
//				event.addReaction("🎲");
				event.reply(game);
			}, failure -> BotLogger.log(event, failure));
		});
	}

//	@Reaction(id = 3, emotes = "🎲")
//	public SteamGame anotherRandomGame(ReactionEvent event) {
//		Object object = event.getReactionRecord().getObject("library");
//
//		if (object instanceof List) {
//			List list = (List)object;
//
//			if (!list.isEmpty() && list.get(0) instanceof SteamGame) {
//				List<SteamGame> library = (List<SteamGame>)list;
//				return library.get(ThreadLocalRandom.current().nextInt(library.size()));
//			}
//		}
//
//		return null;
//	}
}
