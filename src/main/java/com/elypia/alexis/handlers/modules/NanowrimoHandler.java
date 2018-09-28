package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.entities.data.Achievement;
import com.elypia.alexis.entities.embedded.NanowrimoLink;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Secret;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.elypia.elyscript.ElyScript;
import org.slf4j.*;

import java.io.IOException;

@Module(name = "nano", aliases = {"nanowrimo", "nano", "nnwm"}, help = "nano.h")
public class NanowrimoHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(NanowrimoHandler.class);

    private Nanowrimo nanowrimo;

    public NanowrimoHandler() {
        nanowrimo = new Nanowrimo();
    }

    @Secret
    @Database
    @Achievements(value = Achievement.NANOWRIMO_AUTHENTICATED, invert = true)
    @Command(name = "nano.auth", aliases = {"authenticate", "auth"}, help = "nano.auth.h")
    @Param(name = "common.username", help = "nano.auth.p.username.h")
    @Param(name = "nano.auth.p.secret", help = "nano.auth.p.secret.h")
    @Param(name = "nano.auth.p.wordcount", help = "nano.auth.p.wordcount.h")
    public void authenticate(JDACommand event, @Length(min = 1, max = 60) String name, String secret, @Limit(min = 0) int wordcount) throws IOException {
        switch (nanowrimo.setWordCount(secret, name, wordcount)) {
            case ERROR_NO_ACTIVE_EVENT: {
                UserData data = UserData.query(event.getMessage().getAuthor().getIdLong());
                data.setNanoLink(new NanowrimoLink(name, secret, false));
                data.grantAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
                data.commit();

                event.replyScript("nano.authenticated");
                return;
            }
            case ERROR_INVALID_USER: {
                event.replyScript("nano.no_user");
                return;
            }
            case ERROR_HASH_MISMATCH: {
                event.replyScript("nano.incorrect");
            }
        }
    }

    @Achievements(Achievement.NANOWRIMO_AUTHENTICATED)
    @Command(name = "nano.revoke", aliases = {"revoke", "decline"}, help = "nano.revoke.h")
    public String revoke(JDACommand event) {
        long id = event.getMessage().getAuthor().getIdLong();

        UserData data = UserData.query(id);
        data.setNanoLink(null);
        data.revokeAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
        data.commit();

        return event.getScript("nano.revoked");
    }

    @Command(name = "nano.info", aliases = {"get", "info"}, help = "nano.info.h")
    @Param(name = "common.username", help = "nano.info.p.name.h")
    public void getUser(JDACommand event, @Length(min = 1, max = 60) String name) {
        nanowrimo.getUser(name, true).queue((user) -> {
            if (user.getError() == null) {
                event.reply(user);
                return;
            }

            switch (user.getError()) {
                case USER_DOES_NOT_EXIST: {
                    event.replyScript("nano.no_user");
                    return;
                }
                case USER_DOES_NOT_HAVE_A_CURRENT_NOVEL: {
                    event.replyScript("nano.info.no_novel");
                }
            }
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
