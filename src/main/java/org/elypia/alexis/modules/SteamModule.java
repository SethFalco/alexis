/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.modules;

import com.google.inject.Inject;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.Handler;
import org.elypia.elypiai.steam.*;
import org.elypia.jdac.alias.*;
import org.slf4j.*;

import java.util.concurrent.ThreadLocalRandom;

@Module(name = "steam", group = "Gaming", aliases = "steam", help = "steam.h")
public class SteamModule implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(SteamModule.class);

	private Steam steam;

	@Inject
	public SteamModule(Steam steam) {
		this.steam = steam;
	}

	@Command(name = "Steam Profile", aliases = {"get", "user", "profile"}, help = "steam.get.h")
	public void displayProfile(
		CommandlerEvent<?> event,
		@Param(name = "c.username", help = "steam.get.p.username.h") String username
	) {
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

	@Command(name = "steam.library.title", aliases = {"lib", "library"}, help = "steam.library.help")
	public void listLibrary(
		CommandlerEvent<?> event,
		@Param(name = "common.username", help = "The username or ID of the user!") String username
	) {
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

//	@Emoji(emotes = "🎲", help = "Reroll for a different game.")
	@Command(name = "steam.random", aliases = {"random", "rand", "game", "r"}, help = "steam.random.h")
	public void randomGame(
		CommandlerEvent<?> event,
		@Param(name = "username", help = "The username or ID of the user!") String username
	) {
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
