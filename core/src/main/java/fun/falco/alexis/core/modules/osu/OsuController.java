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

package fun.falco.alexis.core.modules.osu;

import java.util.Objects;

import javax.inject.Inject;
import javax.validation.constraints.Size;

import fun.falco.alexis.core.i18n.AlexisMessages;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.osu.Osu;
import org.elypia.elypiai.osu.data.OsuMode;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class OsuController {

    private final AlexisMessages messages;
    private final Osu osu;
    private final MessageSender sender;

    @Inject
    public OsuController(AlexisMessages messages, OsuConfig config, MessageSender sender) {
        this.messages = Objects.requireNonNull(messages);
        this.osu = new Osu(config.getApiKey());
        this.sender = Objects.requireNonNull(sender);
    }

    @StandardCommand
    public void get(@Param @Size(min = 3, max = 15) String username, @Param("osu") OsuMode mode) {
        var scopeToContextualInstances = AsyncUtils.copyContext();

        osu.getPlayer(username, mode).subscribe(
            (player) -> {
                var requestContext = AsyncUtils.applyContext(scopeToContextualInstances);
                sender.send(player);
                requestContext.deactivate();
            },
            (ex) -> {
                var requestContext = AsyncUtils.applyContext(scopeToContextualInstances);
                sender.send(messages.genericNetworkError());
                requestContext.deactivate();
            },
            () -> {
                var requestContext = AsyncUtils.applyContext(scopeToContextualInstances);
                sender.send(messages.playerNotFound(username));
                requestContext.deactivate();
            });
    }
}
