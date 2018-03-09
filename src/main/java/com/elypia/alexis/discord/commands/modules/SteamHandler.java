package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.annotation.Reaction;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.steam.Steam;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

@Module (
	aliases = "Steam",
	help = "Integration with the popular DRM, Steam!"
)
public class SteamHandler extends CommandHandler {

	private static final String PLAYTIME_FORMAT = "%,d hrs";
	private static final TimeUnit PREFERRED_UNIT = TimeUnit.HOURS;

	private Steam steam;

	public SteamHandler(Document document) {
		this(document.getString("api_key"));
	}

	public SteamHandler(String apiKey) {
		steam = new Steam(apiKey);
	}

	@Command (
		aliases = {"get", "user", "profile"},
		help = "Get information on a Steam user!",
		params = {
			@Parameter (
				param = "user",
				help = "The username or ID of the user!",
				type = String.class
			)
		}
	)
	public void displayProfile(CommandEvent event) {
		String[] params = event.getParams();

		steam.getUser(params[0], user -> {
			EmbedBuilder builder = new EmbedBuilder();

			builder.setTitle(user.getName(), user.getProfileURL());
			builder.setThumbnail(user.getAvatarFull());

			event.reply(builder);
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Command(
		aliases = {"lib", "library"},
		help = "Get a players library orderd by recent playtime, then playtime!",
		params = {
			@Parameter (
				param = "user",
				help = "The username or ID of the user!",
				type = String.class
			)
		}
	)
	public void listLibrary(CommandEvent event) {
		String[] params = event.getParams();

		steam.getUser(params[0], user -> {
			steam.getLibrary(user, library -> {
				Object[][] games = new Object[library.size() + 1][3];

				games[0][0] = "Title";
				games[0][1] = "Total Playtime";
				games[0][2] = "Recent Playtime";

				for (int i = 1; i < library.size(); i++) {
					SteamGame game = library.get(i);

					games[i][0] = game.getName();
					games[i][1] = game.getTotalPlaytime(PREFERRED_UNIT);
					games[i][2] = game.getRecentPlaytime(PREFERRED_UNIT);
				}

//				String message = ElyUtils.generateTable(1992, games);
//				event.reply(String.format("```\n%s\n```", message));
			}, failure -> {
				BotUtils.unirestFailure(failure, event);
			});
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Command(
		aliases = {"random", "rand", "game", "r"},
		help = "Select a random game from the players library!",
		params = {
			@Parameter (
				param = "user",
				help = "The username or ID of the user!",
				type = String.class
			)
		},
		reactions = {"🎲"}
	)
	public void randomGame(CommandEvent event) {
		String[] params = event.getParams();

		steam.getUser(params[0], user -> {
			steam.getLibrary(user, library -> {
				SteamGame game = library.get(ElyUtils.RAND.nextInt(library.size()));

				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle(game.getName(), game.getGameUrl());
				builder.setThumbnail(game.getIconUrl());

				String totalPlaytime = String.format(PLAYTIME_FORMAT, game.getTotalPlaytime(PREFERRED_UNIT));
				builder.addField("Total Playtime", totalPlaytime, true);

				String recentPlaytime = String.format(PLAYTIME_FORMAT, game.getRecentPlaytime(PREFERRED_UNIT));
				builder.addField("Recent Playtime", recentPlaytime, true);

				builder.setImage(game.getLogoUrl());
				event.reply(builder);
			}, failure -> {
				BotUtils.unirestFailure(failure, event);
			});
		}, failure -> {
			BotUtils.unirestFailure(failure, event);
		});
	}

	@Reaction (
		aliases = "🎲",
		command = "random"
	)
	public void anotherRandomGame(CommandEvent event) {
		randomGame(event);
	}
}
