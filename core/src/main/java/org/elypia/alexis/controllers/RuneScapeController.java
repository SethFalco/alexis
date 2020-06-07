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

import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.models.QuestStatusModel;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.runescape.*;
import org.elypia.elypiai.runescape.data.*;
import org.elypia.retropia.core.exceptions.FriendlyException;
import org.jboss.weld.context.api.ContextualInstance;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class RuneScapeController {

	private static final Logger logger = LoggerFactory.getLogger(RuneScapeController.class);

	private final AlexisMessages messages;

	/** For responding to commands in other threads. */
    private final MessageSender sender;

	private final RuneScape runescape;

	@Inject
	public RuneScapeController(AlexisMessages messages, MessageSender sender) {
        this.messages = messages;
        this.sender = sender;
		this.runescape = new RuneScape();
	}

    @StandardCommand
	public void getPlayerInfo(@Param @Size(min = 1, max = 12) String username) {
        var contextCopy = AsyncUtils.copyContext();

        runescape.getUser(username).queue((player) -> {
            var context = AsyncUtils.applyContext(contextCopy);
            sender.send(player);
            context.deactivate();
        }, (ex) -> onRuneScapeException(username, ex, contextCopy));
    }

    @StandardCommand
	public void getPlayerQuestStatuses(@Param @Size(min = 1, max = 12) String username) {
        var contextCopy = AsyncUtils.copyContext();

		runescape.getQuestStatuses(username).queue((optQuests) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (optQuests.isEmpty())
                sender.send("No quests were found for this player.");
            else {
                List<QuestStats> quests = optQuests.get();
                Collections.sort(quests);

                Map<CompletionStatus, List<QuestStats>> groupedQuests = quests.stream()
                    .collect(Collectors.groupingBy(QuestStats::getStatus));

                QuestStatusModel model = new QuestStatusModel(username, groupedQuests);
                sender.send(model);
            }

            context.deactivate();
        }, (ex) -> onRuneScapeException(username, ex, contextCopy));
	}

	private void onRuneScapeException(String username, Throwable ex, Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> contextCopy) {
        var context = AsyncUtils.applyContext(contextCopy);

        if (!(ex instanceof FriendlyException))
            sender.send("Something went wrong.");
        else {
            FriendlyException fex = (FriendlyException)ex;
            String tag = fex.getTag();

            if (tag.equals(RuneScapeError.PROFILE_PRIVATE.getName()))
                sender.send(messages.runescapeMetricsSetToPrivate(username));
            else if(tag.equals(RuneScapeError.NOT_A_MEMBER.getName()))
                sender.send(messages.runescapeMetricsNotActiveAccount(username));
            else
                sender.send(messages.runescapeMetricsUserNotFound(username));
        }

        context.deactivate();
    }
}
