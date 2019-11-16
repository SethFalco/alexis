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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.elypia.commandler.api.Controller;
import org.elypia.elypiai.runescape.*;
import org.hibernate.validator.constraints.Length;
import org.slf4j.*;

import javax.inject.*;
import java.io.IOException;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class RuneScapeController implements Controller {

	private static final Logger logger = LoggerFactory.getLogger(RuneScapeController.class);

	private final RuneScape runescape;

	@Inject
	public RuneScapeController(final RuneScape runescape) {
		this.runescape = runescape;
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
