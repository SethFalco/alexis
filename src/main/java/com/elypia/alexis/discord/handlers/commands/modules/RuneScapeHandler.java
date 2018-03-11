package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.runescape.QuestStats;
import com.elypia.elypiai.runescape.RuneScape;
import com.elypia.elypiai.runescape.data.QuestStatus;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Collection;

@Module (
	aliases = {"RuneScape", "RS"},
	help = "Integration with the popular MMORPG, RuneScape!"
)
public class RuneScapeHandler extends CommandHandler {

	private RuneScape runescape;

	public RuneScapeHandler() {
		runescape = new RuneScape();
	}

	@Override
	public boolean test() {

		return false;
	}

	@Command (
		aliases = "status",
		help = "The total number of created accounts."
	)
	public void displayStatus(MessageEvent event) {

	}

	@Command (
		aliases = "stats",
		help = "Get stats for a particular user.",
		params = {
			@Parameter(param = "username", help = "RuneScape players username.", type = String.class)
		}
	)
	public void getPlayerStats(MessageEvent event) {

	}

	@Command (
		aliases = {"quests", "quest", "q"},
		help = "Get status of all quests for a user.",
		params = {
			@Parameter (
				param = "username",
				help = "RuneScape players username.",
				type = String.class
			)
		}
	)
	public void getQuests(MessageEvent event) {
		String param = event.getParams()[0];

		runescape.getQuestStatuses(param, result -> {
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
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}
}
