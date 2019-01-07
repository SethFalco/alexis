package com.elypia.alexis.commandler.modules.gaming;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.elypiai.steam.*;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.util.concurrent.ThreadLocalRandom;

@Module(id = "Steam", group = "Gaming", aliases = "steam", help = "steam.help")
public class SteamModule extends JDACHandler {

	private static final Logger logger = LoggerFactory.getLogger(SteamModule.class);

	private Steam steam;

	/**
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link ModuleData} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 * @return Returns if the {@link #test()} for this module passed.
	 */
	public SteamModule(Commandler<GenericMessageEvent, Message> commandler) {
		super(commandler);
		steam = new Steam(Alexis.config.getApiCredentials().getSteam());
	}

	@Command(id = "Steam Profile", aliases = {"get", "user", "profile"}, help = "steam.get.help")
	@Param(id = "common.username", help = "steam.get.p.username.help")
	public void displayProfile(JDACEvent event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.send("steam.get.no_user");
				return;
			}

			steam.getUsers(search.getId()).queue(users -> {
				SteamUser user = users.get(0);
				event.send(user);
			}, (ex) -> logger.error("Failed to perform HTTP request!", ex));
		});
	}

	@Command(id = "steam.library.title", aliases = {"lib", "library"}, help = "steam.library.help")
	@Param(id = "common.username", help = "The username or ID of the user!")
	public void listLibrary(JDACEvent event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.send("Sorry, I couldn't find that user.");
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

	@Command(id = "Game Roulette", aliases = {"random", "rand", "game", "r"}, help = "Select a random game from the players library!")
	@Param(id = "username", help = "The username or ID of the user!")
//	@Emoji(emotes = "🎲", help = "Reroll for a different game.")
	public void randomGame(JDACEvent event, String username) {
		steam.getIdFromVanityUrl(username).queue((search) -> {
			if (!search.isSuccess()) {
				event.send("Sorry, I couldn't find that user.");
				return;
			}

			steam.getLibrary(search.getId()).queue(library -> {
//				event.storeObject("library", library);

				SteamGame game = library.get(ThreadLocalRandom.current().nextInt(library.size()));
//				event.addReaction("🎲");
				event.send(game);
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
