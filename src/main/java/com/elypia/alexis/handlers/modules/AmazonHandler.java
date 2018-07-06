//package com.elypia.alexis.handlers.modules;
//
//import com.elypia.alexis.config.AmazonDetails;
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.annotations.*;
//import com.elypia.commandler.events.AbstractEvent;
//import com.elypia.commandler.modules.CommandHandler;
//import com.elypia.elypiai.amazon.*;
//
//import java.security.InvalidKeyException;
//import java.util.*;
//
//@Module(name = "Amazon", aliases = "amazon", description = "Share links and support Elypia! We get a cut from purchases!")
//public class AmazonHandler extends CommandHandler {
//
//    private Amazon amazon;
//
//    public AmazonHandler(List<AmazonDetails> details) {
//        Objects.requireNonNull(details);
//
//        AmazonDetails detail = details.get(0);
//
//        try {
//            amazon = new Amazon(detail.getKey(), detail.getSecret(), detail.getTag(), detail.getEndpoint());
//        } catch (InvalidKeyException ex) {
//            ex.printStackTrace();
//            enabled = false;
//        }
//    }
//
//    @Default
//    @Command(name = "Search Amazon", aliases = {"search", "get"}, help = "Search Amazon for a product and share it.")
//    @Param(name = "query", help = "Name of the product you're after.")
//    public void getItem(AbstractEvent event, String query) {
//        amazon.getItems(query, (result) -> {
//            event.reply(!result.isEmpty() ? result.get(0) : "Sorry, Amazon returned no results. :c");
//        }, (failure) -> BotUtils.sendHttpError(event, failure));
//    }
//}
