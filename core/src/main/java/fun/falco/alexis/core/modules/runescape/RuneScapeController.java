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

package fun.falco.alexis.core.modules.runescape;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.Size;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.runescape.QuestStatus;
import org.elypia.elypiai.runescape.RuneScape;
import org.elypia.elypiai.runescape.data.CompletionStatus;
import org.elypia.elypiai.runescape.data.RuneScapeError;
import org.elypia.retropia.core.exceptions.FriendlyException;
import org.jboss.weld.context.api.ContextualInstance;

import fun.falco.alexis.core.i18n.AlexisMessages;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class RuneScapeController {

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

        runescape.getUser(username).subscribe((player) -> {
            var context = AsyncUtils.applyContext(contextCopy);
            sender.send(player);
            context.deactivate();
        }, (ex) -> onRuneScapeException(username, ex, contextCopy));
    }

    @StandardCommand
    public void getPlayerQuestStatuses(@Param @Size(min = 1, max = 12) String username) {
        var contextCopy = AsyncUtils.copyContext();

        runescape.getQuestStatuses(username).subscribe((quests) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (quests.getQuestStatuses().isEmpty()) {
                sender.send("No quests were found for this player.");
            } else {
                Map<CompletionStatus, List<QuestStatus>> groupedQuests = new EnumMap<>(CompletionStatus.class);

                for (CompletionStatus status : CompletionStatus.values()) {
                    groupedQuests.put(status, quests.getByCompletionStatus(status));
                }

                QuestStatusModel model = new QuestStatusModel(username, groupedQuests);
                sender.send(model);
            }

            context.deactivate();
        }, (ex) -> onRuneScapeException(username, ex, contextCopy));
    }

    private void onRuneScapeException(String username, Throwable ex,
            Map<Class<? extends Annotation>, Collection<ContextualInstance<?>>> contextCopy) {
        var context = AsyncUtils.applyContext(contextCopy);

        if (!(ex instanceof FriendlyException)) {
            sender.send("Something went wrong.");
        } else {
            FriendlyException fex = (FriendlyException) ex;
            String tag = fex.getTag();

            if (tag.equals(RuneScapeError.PROFILE_PRIVATE.getName())) {
                sender.send(messages.runescapeMetricsSetToPrivate(username));
            } else if (tag.equals(RuneScapeError.NOT_A_MEMBER.getName())) {
                sender.send(messages.runescapeMetricsNotActiveAccount(username));
            } else {
                sender.send(messages.runescapeMetricsUserNotFound(username));
            }
        }

        context.deactivate();
    }
}
