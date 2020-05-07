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

import org.elypia.alexis.config.ApiConfig;
import org.elypia.commandler.api.Controller;
import org.elypia.elypiai.steam.*;
import org.hibernate.validator.constraints.Length;
import org.slf4j.*;

import javax.inject.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class SteamController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(SteamController.class);

	/** The maxmimum length a Steam username can be. */
	private static final int MAX_USERNAME_LENGTH = 32;

	/** Access the Steam API */
	private final Steam steam;

	@Inject
	public SteamController(final ApiConfig config) {
		this.steam = new Steam(config.getSteam());
	}

	public Object getId(@Length(max = MAX_USERNAME_LENGTH) String username) throws IOException {
		Optional<SteamSearch> optSearch = steam.getIdFromVanityUrl(username).complete();
		String errorText = "Sorry, I couldn't find that user on Steam. If it helps, you should give the ID, or what would be at the end of their custom URL.";

		if (optSearch.isEmpty())
			return errorText;

		SteamSearch search = optSearch.get();

		if (!search.isSuccess())
			return errorText;

		return search.getId();
	}

	public Object getPlayerById(long steamId) throws IOException {
		Optional<List<SteamUser>> optUsers = steam.getUsers(steamId).complete();

		if (optUsers.isEmpty())
			return "I found the user, but I was unable to access their profile.";

		List<SteamUser> users = optUsers.get();
		return users.get(0);
	}

	public Object getPlayerByName(@Length(max = MAX_USERNAME_LENGTH) String username) throws IOException {
		Object steamId = getId(username);

		if (!(steamId instanceof Long))
			return steamId;

		return getPlayerById((long)steamId);
	}

	public Object getRandomGame(@Length(max = MAX_USERNAME_LENGTH) String username) throws IOException {
		Object steamId = getId(username);

		if (!(steamId instanceof Long))
			return steamId;

		Optional<List<SteamGame>> optGames = steam.getLibrary((long)steamId).complete();

		if (optGames.isEmpty())
			return "I found the user, but I was unable to access their library.";

		List<SteamGame> games = optGames.get();
		return games.get(ThreadLocalRandom.current().nextInt(games.size()));
	}
}
