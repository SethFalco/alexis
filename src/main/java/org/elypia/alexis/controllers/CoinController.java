package org.elypia.alexis.controllers;

import org.elypia.commandler.api.Controller;

import javax.inject.Singleton;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class CoinController implements Controller {

    public String flipCoin() {
        return ThreadLocalRandom.current().nextBoolean() ? "Heads" : "Tails";
    }
}
