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

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.elypia.commandler.api.Controller;
import org.elypia.elypiai.runescape.*;
import org.hibernate.validator.constraints.Length;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class RuneScapeController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(RuneScapeController.class);

	private final RuneScape runescape;

	@Inject
	public RuneScapeController() {
		this.runescape = new RuneScape();
	}

	public void status() {

	}

//	public void stats(@Length(min = 1, max = 12) String username) {
//
//	}

	public Object quests(@Length(min = 1, max = 12) String username) throws IOException {
		Optional<List<QuestStats>> optQuests = runescape.getQuestStatuses(username).complete();

		if (optQuests.isEmpty())
		    return "No quests were found for this player.";

        EmbedBuilder builder = new EmbedBuilder();

        List<QuestStats> quests = optQuests.get();
        Collections.sort(quests);

        StringJoiner completed = new StringJoiner(", ");
        StringJoiner started = new StringJoiner(", ");
        StringJoiner notStarted = new StringJoiner(", ");
        int totalCompleted = 0, totalStarted = 0, totalNotStarted = 0;

        for (QuestStats stats : quests) {
            switch (stats.getStatus()) {
                case COMPLETED:
                    completed.add(stats.getTitle());
                    totalCompleted++;
                    break;
                case STARTED:
                    started.add(stats.getTitle());
                    totalStarted++;
                    break;
                case NOT_STARTED:
                    notStarted.add(stats.getTitle());
                    totalNotStarted++;
                    break;
                case UNKNOWN:
                default:
                    throw new IllegalStateException("Unknown completion state occured.");
            }
        }

        String completedString = completed.toString(), startedString = started.toString(), notStartedString = notStarted.toString();

        if (completedString.length() > MessageEmbed.VALUE_MAX_LENGTH)
            completedString = completedString.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";
        if (startedString.length() > MessageEmbed.VALUE_MAX_LENGTH)
            startedString = startedString.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";
        if (notStartedString.length() > MessageEmbed.VALUE_MAX_LENGTH)
            notStartedString = notStartedString.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";

        builder.addField("Completed (" + totalCompleted + ")", completedString, false);
        builder.addField("Started (" + totalStarted + ")", startedString, false);
        builder.addField("Not Started (" + totalNotStarted + ")", notStartedString, false);

        return builder.build();
	}
}
