//package com.elypia.alexis.handlers.modules;
//
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.annotations.*;
//import com.elypia.commandler.events.*;
//import com.elypia.commandler.modules.CommandHandler;
//import com.elypia.elypiai.steam.*;
//import com.elypia.elypiai.utils.ElyUtils;
//import net.dv8tion.jda.core.EmbedBuilder;
//
//import java.util.List;
//
//@Module(name = "Steam", aliases = "steam", description = "Integration with the popular DRM, Steam!")
//public class SteamHandler extends CommandHandler {
//
//	private Steam steam;
//
//	public SteamHandler(String apiKey) {
//		steam = new Steam(apiKey);
//	}
//
//	@Command(name = "Steam Profile", aliases = {"get", "user", "profile"}, help = "Get information on a Steam user!")
//	@Param(name = "username", help = "The name you'd find at the end of their custom URL!")
//	public void displayProfile(CommandEvent event, String username) {
//		steam.getUser(username, user -> {
//			if (user == null) {
//				event.reply("I don't think that user exists?");
//				return;
//			}
//
//			EmbedBuilder builder = new EmbedBuilder();
//
//			builder.setTitle(user.getUsername(), user.getProfileURL());
//			builder.setThumbnail(user.getAvatar());
//
//			event.reply(builder);
//		}, failure -> BotUtils.sendHttpError(event, failure));
//	}
//
//	@Command(name = "Steam Library", aliases = {"lib", "library"}, help = "Get a players library orderd by recent playtime, then playtime!")
//	@Param(name = "username", help = "The username or ID of the user!")
//	public void listLibrary(CommandEvent event, String username) {
//		steam.getUser(username, user -> {
//			user.getLibrary(library -> {
//				if (user == null) {
//					event.reply("I don't think that user exists?");
//					return;
//				}
//
//				Object[][] games = new Object[library.size() + 1][3];
//
//				games[0][0] = "Title";
//				games[0][1] = "Total Playtime";
//				games[0][2] = "Recent Playtime";
//
//				for (int i = 1; i < library.size(); i++) {
//					SteamGame game = library.get(i);
//
//					games[i][0] = game.getName();
//					games[i][1] = game.getTotalPlaytime();
//					games[i][2] = game.getRecentPlaytime();
//				}
//
////				String message = ElyUtils.generateTable(1992, games);
////				event.reply(String.format("```\n%s\n```", message));
//			}, failure -> BotUtils.sendHttpError(event, failure));
//		}, failure -> BotUtils.sendHttpError(event, failure));
//	}
//
//	@Command(id = 3, name = "Game Roulette", aliases = {"random", "rand", "game", "r"}, help = "Select a random game from the players library!")
//	@Param(name = "username", help = "The username or ID of the user!")
//	@Emoji(emotes = "🎲", help = "Reroll for a different game.", auto = false)
//	public void randomGame(CommandEvent event, String username) {
//		steam.getUser(username, user -> {
//			if (user == null) {
//				event.reply("I don't think that user exists?");
//				return;
//			}
//
//			user.getLibrary(library -> {
//				event.storeObject("library", library);
//
//				SteamGame game = library.get(ElyUtils.RANDOM.nextInt(library.size()));
//				event.addReaction("🎲");
//				event.reply(game);
//			}, failure -> BotUtils.sendHttpError(event, failure));
//		}, failure -> BotUtils.sendHttpError(event, failure));
//	}
//
//	@Reaction(id = 3, emotes = "🎲")
//	public SteamGame anotherRandomGame(ReactionEvent event) {
//		List<SteamGame> library = (List<SteamGame>)event.getReactionRecord().getObject("library");
//		return library.get(ElyUtils.RANDOM.nextInt(library.size()));
//	}
//}
