/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.core.modules.steam;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.validation.constraints.Size;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.steam.Steam;
import org.elypia.elypiai.steam.SteamGame;

import fun.falco.alexis.core.i18n.AlexisMessages;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class SteamController {

    /** The minimum length a steam username can be. */
    protected static final int MIN_NAME_LENGTH = 1;

    /** The maximum length a Steam username can be. */
    protected static final int MAX_NAME_LENGTH = 32;

    /** Access the Steam API. */
    private final Steam steam;

    /** Strings that Alexis will say. */
    private final AlexisMessages messages;

    private final MessageSender sender;

    @Inject
    public SteamController(final SteamConfig config, final AlexisMessages messages, final MessageSender sender) {
        this.steam = new Steam(config.getApiKey());
        this.messages = messages;
        this.sender = sender;
    }

    @StandardCommand
    public void getId(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
        var contextCopy = AsyncUtils.copyContext();

        steam.getIdFromVanityUrl(username).subscribe((steamSearch) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (!steamSearch.isSuccess()) {
                sender.send(messages.steamUserNotFound());
            } else {
                sender.send(messages.steamReturnSteam64Id(username, steamSearch.getId()));
            }

            context.deactivate();
        });
    }

    @StandardCommand
    public void getPlayerById(@Param long steamId) {
        var contextCopy = AsyncUtils.copyContext();

        steam.getUsers(steamId).subscribe((users) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (users.isEmpty()) {
                sender.send(messages.steamProfilePrivate());
            } else {
                sender.send(users.get(0));
            }

            context.deactivate();
        });
    }

    @StandardCommand
    public void getPlayerByName(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
        var contextCopy = AsyncUtils.copyContext();

        steam.getIdFromVanityUrl(username).subscribe(
            (steamSearch) -> {
                var context = AsyncUtils.applyContext(contextCopy);

                if (!steamSearch.isSuccess()) {
                    messages.steamUserNotFound();
                } else {
                    getPlayerById(steamSearch.getId());
                }

                context.deactivate();
            }
        );
    }

    /**
     * @param username Steam username.
     */
    @StandardCommand
    public void getRandomGame(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
        withLibrary(username, (games) -> {
            sender.send(games.get(ThreadLocalRandom.current().nextInt(games.size())));
        });
    }

    /**
     * Generic method to perform logic that requires a
     * Steam users library by their username.
     *
     * @param username Username of the player on Steam.
     * @param callback Callback to invoke with the list of games the user has played.
     */
    private void withLibrary(String username, Consumer<List<SteamGame>> callback) {
        var contextCopy = AsyncUtils.copyContext();

        steam.getIdFromVanityUrl(username).subscribe((steamSearch) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (!steamSearch.isSuccess()) {
                sender.send(messages.steamUserNotFound());
            } else {
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
