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
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.elypiai.steam.*;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class SteamController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(SteamController.class);

	/** The minimum length a steam username can be. */
	protected static final int MIN_NAME_LENGTH = 1;

	/** The maxmimum length a Steam username can be. */
	protected static final int MAX_NAME_LENGTH = 32;

	/** Access the Steam API */
	protected final Steam steam;

	/** Strings that Alexis will say. */
	protected final AlexisMessages messages;

	@Inject
	public SteamController(final ApiConfig config, final AlexisMessages messages) {
		this.steam = new Steam(config.getSteam());
		this.messages = messages;
	}

	@StandardCommand
	public String getId(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) throws IOException {
		SteamSearch search = steam.getIdFromVanityUrl(username).complete();

		if (!search.isSuccess())
			return messages.steamUserNotFound();

		return messages.steamReturnSteam64Id(username, search.getId());
	}

	@StandardCommand
	public Object getPlayerById(@Param long steamId) throws IOException {
		List<SteamUser> users = steam.getUsers(steamId).complete();

		if (users.isEmpty())
			return messages.steamProfilePrivate();

		return users.get(0);
	}

	@StandardCommand
	public Object getPlayerByName(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) throws IOException {
		SteamSearch search = steam.getIdFromVanityUrl(username).complete();

		if (!search.isSuccess())
			return messages.steamUserNotFound();

		return getPlayerById(search.getId());
	}

	@StandardCommand
	public Object getRandomGame(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) throws IOException {
		SteamSearch search = steam.getIdFromVanityUrl(username).complete();

		if (!search.isSuccess())
			return messages.steamUserNotFound();

		Optional<List<SteamGame>> optGames = steam.getLibrary(search.getId()).complete();

		if (optGames.isEmpty())
			return messages.steamLibraryPrivate();

		List<SteamGame> games = optGames.get();
		return games.get(ThreadLocalRandom.current().nextInt(games.size()));
	}
}
