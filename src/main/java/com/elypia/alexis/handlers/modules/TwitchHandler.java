//package com.elypia.alexis.handlers.modules;
//
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.annotations.*;
//import com.elypia.commandler.events.AbstractEvent;
//import com.elypia.commandler.modules.CommandHandler;
//import com.elypia.elypiai.twitch.*;
//import com.elypia.elypiai.twitch.data.BroadcasterType;
//import net.dv8tion.jda.core.EmbedBuilder;
//
//@Module(name = "Twitch", aliases = "twitch", description = "Get information on various streamers!")
//public class TwitchHandler extends CommandHandler {
//
//    private Twitch twitch;
//
//    public TwitchHandler(String clientId) {
//        twitch = new Twitch(clientId);
//    }
//
//    @Command(name = "Streamer Info", aliases = {"get", "info"}, help = "Get information on streamers.")
//    @Param(name = "usernames", help = "The user(s) to retrieve.")
//    public void getUser(AbstractEvent event, String[] usernames) {
//        twitch.getUsers(usernames, result -> {
//            if (result.isEmpty()) {
//                event.reply("Sorry I couldn't find any of the users.");
//                return;
//            }
//
//            EmbedBuilder builder = new EmbedBuilder();
//
//            if (result.size() == 1) {
//                TwitchUser user = result.iterator().next();
//
//                builder.setAuthor(user.getDisplayName(), user.getUrl());
//                builder.setThumbnail(user.getAvatar());
//                builder.setDescription(user.getDescription());
//                builder.addField("Type", BroadcasterType.AFFILIATE.toString(), true);
//                builder.addField("Total Views", String.format("%,d", user.getViewCount()), true);
//            } else {
//                for (TwitchUser user : result) {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append( user.getUrl()).append("\n");
//
//                    if (user.getDescription() != null)
//                        sb.append(user.getDescription() + "\n");
//
//                    sb.append("Total Views: " + String.format("%,d", user.getViewCount()));
//
//                    builder.addField(user.getDisplayName(), sb.toString(), false);
//                }
//            }
//
//            if (result.size() != usernames.length)
//                builder.setFooter("Sorry, I didn't find every user you specified but I returned what I could.", null);
//
//            event.reply(builder);
//        }, failure -> BotUtils.sendHttpError(event, failure));
//    }
//}
