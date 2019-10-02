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

import com.google.inject.*;
import net.dv8tion.jda.api.EmbedBuilder;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.Handler;
import org.elypia.elypiai.runescape.*;
import org.slf4j.*;

import java.util.Collection;

@Singleton
@Module(name = "RuneScape", group = "Gaming", aliases = {"runescape", "rs"}, help = "rs.help")
public class RuneScapeModule implements Handler {

	private static final Logger logger = LoggerFactory.getLogger(RuneScapeModule.class);

	private RuneScape runescape;

	@Inject
	public RuneScapeModule(RuneScape runescape) {
		this.runescape = runescape;
	}

	// TODO: Not implemented yet.
	@Command(name = "Status", aliases = "status", help = "rs.status.help")
	public void displayStatus() {
		//
	}

	// TODO: Not implemented yet.
	@Command(name = "Player Stats", aliases = "stats", help = "rs.stats.help")
	public void getPlayerStats(
		@Param(name = "common.username", help = "rs.stats.username.help") String username
	) {

	}

	@Command(name = "rs.quests.title", aliases = {"quests", "quest", "q"}, help = "rs.quests.help")
	public void getQuests(
		@Param(name = "common.username", help = "rs.stats.p.username.help")	String username
	) {
		runescape.getQuestStatuses(username).queue(result -> {
			EmbedBuilder builder = new EmbedBuilder();

			Collection<QuestStats> completedQuests = result.getQuests(QuestStatus.COMPLETED);
			Collection<QuestStats> startedQuests = result.getQuests(QuestStatus.STARTED);
			Collection<QuestStats> notStartedQuests = result.getQuests(QuestStatus.NOT_STARTED);

            String[] completed = ElyUtils.toStringArray(completedQuests);
            String[] started = ElyUtils.toStringArray(startedQuests);
			String[] notStarted = ElyUtils.toStringArray(notStartedQuests);

			builder.setTitle("Quest Stats for " + result.getUsername() + "!");

			builder.addField("Completed", String.join("\n", completed), false);
			builder.addField("Started", String.join("\n", started), false);
			builder.addField("Not Started", String.join("\n", notStarted), false);
		}, ex -> logger.error("Failed to perform HTTP request!", ex));
	}
}
