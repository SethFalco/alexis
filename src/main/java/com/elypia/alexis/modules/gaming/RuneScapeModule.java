package com.elypia.alexis.modules.gaming;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.elypiai.runescape.RuneScape;
import com.elypia.jdac.alias.*;

@Module(id = "RuneScape", group = "Gaming", aliases = {"runescape", "rs"}, help = "rs.help")
public class RuneScapeModule extends JDACHandler {

	private RuneScape runescape;

	public RuneScapeModule() {
		runescape = new RuneScape();
	}

	@Command(id = "Status", aliases = "status", help = "rs.status.help")
	public void displayStatus(JDACEvent event) {

	}

	@Command(id = "Player Stats", aliases = "stats", help = "rs.stats.help")
	@Param(id = "common.username", help = "rs.stats.username.help")
	public void getPlayerStats(JDACEvent event, String username) {

	}

//	@Command(name = "rs.quests.title", aliases = {"quests", "quest", "q"}, help = "rs.quests.help")
//	@Param(name = "common.username", help = "rs.stats.p.username.help")
//	public void getQuests(AbstractEvent event, String username) {
//		runescape.getQuestStatuses(username, result -> {
//			EmbedBuilder builder = new EmbedBuilder();
//
//			Collection<QuestStats> completedQuests = result.getQuests(QuestStatus.COMPLETED);
//			Collection<QuestStats> startedQuests = result.getQuests(QuestStatus.STARTED);
//			Collection<QuestStats> notStartedQuests = result.getQuests(QuestStatus.NOT_STARTED);
//
//            String[] completed = ElyUtils.toStringArray(completedQuests);
//            String[] started = ElyUtils.toStringArray(startedQuests);
//			String[] notStarted = ElyUtils.toStringArray(notStartedQuests);
//
//			builder.setTitle("Quest Stats for " + result.getUsername() + "!");
//
//			builder.addField("Completed", String.join("\n", completed), false);
//			builder.addField("Started", String.join("\n", started), false);
//			builder.addField("Not Started", String.join("\n", notStarted), false);
//		}, failure -> BotUtils.sendHttpError(event, failure));
//	}
}
