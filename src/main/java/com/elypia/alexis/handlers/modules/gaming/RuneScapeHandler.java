package com.elypia.alexis.handlers.modules.gaming;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.runescape.RuneScape;

@Module(name = "rs.title", group = "Gaming", aliases = {"runescape", "rs"}, help = "rs.help")
public class RuneScapeHandler extends JDAHandler {

	private RuneScape runescape;

	public RuneScapeHandler() {
		runescape = new RuneScape();
	}

	@Command(name = "rs.status.title", aliases = "status", help = "rs.status.help")
	public void displayStatus(JDACommand event) {

	}

	@Command(name = "rs.stats.title", aliases = "stats", help = "rs.stats.help")
	@Param(name = "common.username", help = "rs.stats.username.help")
	public void getPlayerStats(JDACommand event, String username) {

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
