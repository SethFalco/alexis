/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.controllers;

import org.elypia.alexis.configuration.ApiConfig;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.steam.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class SteamController {

	private static final Logger logger = LoggerFactory.getLogger(SteamController.class);

	/** The minimum length a steam username can be. */
	protected static final int MIN_NAME_LENGTH = 1;

	/** The maxmimum length a Steam username can be. */
	protected static final int MAX_NAME_LENGTH = 32;

	/** Access the Steam API */
	private final Steam steam;

	/** Strings that Alexis will say. */
	private final AlexisMessages messages;

	private final MessageSender sender;

	@Inject
	public SteamController(final ApiConfig config, final AlexisMessages messages, final MessageSender sender) {
		this.steam = new Steam(config.getSteam());
		this.messages = messages;
		this.sender = sender;
	}

	@StandardCommand
	public void getId(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
		var contextCopy = AsyncUtils.copyContext();

		steam.getIdFromVanityUrl(username).subscribe((steamSearch) -> {
			var context = AsyncUtils.applyContext(contextCopy);

			if (!steamSearch.isSuccess())
				sender.send(messages.steamUserNotFound());
			else
				sender.send(messages.steamReturnSteam64Id(username, steamSearch.getId()));

			context.deactivate();
		});
	}

	@StandardCommand
	public void getPlayerById(@Param long steamId) {
		var contextCopy = AsyncUtils.copyContext();

		steam.getUsers(steamId).subscribe((users) -> {
			var context = AsyncUtils.applyContext(contextCopy);

			if (users.isEmpty())
				sender.send(messages.steamProfilePrivate());
			else
				sender.send(users.get(0));

			context.deactivate();
		});
	}

	@StandardCommand
	public void getPlayerByName(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
		var contextCopy = AsyncUtils.copyContext();

		steam.getIdFromVanityUrl(username).subscribe(
			(steamSearch) -> {
				var context = AsyncUtils.applyContext(contextCopy);

				if (!steamSearch.isSuccess())
					messages.steamUserNotFound();
				else
					getPlayerById(steamSearch.getId());

				context.deactivate();
			}
		);
	}

	/**
	 * @param username A users Steam username.
	 */
	@StandardCommand
	public void getRandomGame(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
		withLibrary(username, (games) -> {
			sender.send(games.get(ThreadLocalRandom.current().nextInt(games.size())));
		});
	}

	/**
	 * Get the top most played games of the player, only
	 * including games they've actually played recently.
	 *
	 * @param username The Steam user to get the library of.
	 */
//	@StandardCommand
	public void getRecentlyPlayedGames(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
		withLibrary(username, (games) -> {
			if (games.isEmpty()) {
				sender.send(messages.steamLibraryEmpty());
				return;
			}

			List<SteamGame> playedGames = games.stream()
				.filter((game) -> game.getRecentPlaytime() > 0)
				.sorted()
				.collect(Collectors.toList());

			if (playedGames.isEmpty()) {
				sender.send(messages.steamNoRecentlyPlayedGamed(username));
				return;
			}

			CategoryChart chart = new CategoryChartBuilder()
				.title("Steam Playtime Stats")
				.xAxisTitle("Game")
				.yAxisTitle("Hours")
				.build();

			List<String> gameNames = playedGames.stream()
				.map(SteamGame::getName)
				.collect(Collectors.toList());

			List<Long> recentPlaytimes = playedGames.stream()
				.map(SteamGame::getRecentPlaytime)
				.collect(Collectors.toList());

			chart.addSeries("Recent Playtime", gameNames, recentPlaytimes);

			final CategoryStyler styler = chart.getStyler();
			styler.setXAxisLabelRotation(90);
			styler.setXAxisLabelAlignmentVertical(Styler.TextAlignment.Right);

			try {
				BitmapEncoder.saveBitmap(chart, "./test_" + username, BitmapEncoder.BitmapFormat.PNG);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Generic method to perform logic that requires a
	 * Steam users library by their username.
	 *
	 * @param username The username of the player on Steam.
	 * @param callback The callback to perform against the list of games the user has played.
	 */
	private void withLibrary(String username, Consumer<List<SteamGame>> callback) {
		var contextCopy = AsyncUtils.copyContext();

		steam.getIdFromVanityUrl(username).subscribe((steamSearch) -> {
			var context = AsyncUtils.applyContext(contextCopy);

			if (!steamSearch.isSuccess())
				sender.send(messages.steamUserNotFound());
			else {
				steam.getLibrary(steamSearch.getId()).subscribe(
					(games) -> {
						var contextCopyII = AsyncUtils.applyContext(contextCopy);
						callback.accept(games);
						contextCopyII.deactivate();
					},
					(error) -> {
						var contextCopyII = AsyncUtils.applyContext(contextCopy);
						sender.send(messages.genericNetworkError());
						contextCopyII.deactivate();
					},
					() -> {
						var contextCopyII = AsyncUtils.applyContext(contextCopy);
						sender.send(messages.steamLibraryPrivate());
						contextCopyII.deactivate();
					}
				);
			}

			context.deactivate();
		});
	}
}
