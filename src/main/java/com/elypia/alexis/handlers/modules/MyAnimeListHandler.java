//package com.elypia.alexis.handlers.modules;
//
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.annotations.*;
//import com.elypia.commandler.events.AbstractEvent;
//import com.elypia.commandler.modules.CommandHandler;
//import com.elypia.elypiai.myanimelist.MyAnimeList;
//
//@Module(name = "My Anime List", aliases = {"myanimelist", "mal", "anime"}, description = "Search through MyAnimeList for animes and managas.")
//public class MyAnimeListHandler extends CommandHandler {
//
//    private MyAnimeList mal;
//
//    public MyAnimeListHandler(String auth) {
//        mal = new MyAnimeList(auth);
//    }
//
//    @Command(name = "Get Anime", aliases = "anime", help = "Get information on an anime.")
//    @Param(name = "animes", help = "A search terms to find shows.")
//    public void getAnime(AbstractEvent event, String anime) {
//        mal.getAnime(anime).queue((result) -> {
//            event.reply(result != null ? result : "Sorry, I didn't find the show you were looking for."));
//        }, ex -> BotUtils.sendHttpError(event, ex));
//    }
//}
