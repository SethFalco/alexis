package com.elypia.alexis.handlers.modules.gaming;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.steam.*;
import org.slf4j.*;

import java.util.concurrent.ThreadLocalRandom;

@Module(name = "steam.title", group = "Gaming", aliases = "steam", help = "steam.help")
public class SteamHandler extends JDAHandler {

	private static final Logger logger = LoggerFactory.getLogger(SteamHandler.class);

	private Steam steam;

	public SteamHandler() {
		steam = new Steam(Alexis.config.getApiCredentials().getSteam());
	}

	@Command(name = "steam.get.title", aliases = {"get", "user", "profile"}, help = "steam.get.help")
	@Param(name = "common.username", help = "steam.get.p.username.help")
	public void displayProfile(JDACommand event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.replyScript("steam.get.no_user");
				return;
			}

			steam.getUsers(search.getId()).queue(users -> {
				SteamUser user = users.get(0);
				event.reply(user);
			}, (ex) -> logger.error("Failed to perform HTTP request!", ex));
		});
	}

	@Command(name = "steam.library.title", aliases = {"lib", "library"}, help = "steam.library.help")
	@Param(name = "common.username", help = "The username or ID of the user!")
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
			}, (ex) -> logger.error("Failed to perform HTTP request!", ex));
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
			}, (ex) -> logger.error("Failed to perform HTTP request!", ex));
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
