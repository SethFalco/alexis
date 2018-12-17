package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.commandler.validation.*;
import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.entities.data.Achievement;
import com.elypia.alexis.entities.embedded.NanowrimoLink;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.elypia.jdac.alias.*;
import org.slf4j.*;

import javax.validation.constraints.*;
import java.io.IOException;

@Module(id = "NaNoWriMo", aliases = {"nanowrimo", "nano", "nnwm"}, help = "nano.h")
public class NanowrimoModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(NanowrimoModule.class);

    private Nanowrimo nanowrimo;

    public NanowrimoModule() {
        nanowrimo = new Nanowrimo();
    }



    @Command(id = "nano.auth", aliases = {"authenticate", "auth"}, help = "nano.auth.h")
    @Param(id = "common.username", help = "nano.auth.p.username.h")
    @Param(id = "nano.auth.p.secret", help = "nano.auth.p.secret.h")
    @Param(id = "nano.auth.p.wordcount", help = "nano.auth.p.wordcount.h")
    public void authenticate(
        @Database @Achievements(value = Achievement.NANOWRIMO_AUTHENTICATED, achieved = false) JDACEvent event,
        @Size(min = 1, max = 60) String name,
        String secret,
        @Min(0) int wordcount
    ) throws IOException {
        switch (nanowrimo.setWordCount(secret, name, wordcount)) {
            case ERROR_NO_ACTIVE_EVENT: {
                UserData data = UserData.query(event.asMessageRecieved().getAuthor().getIdLong());
                data.setNanoLink(new NanowrimoLink(name, secret, false));
                data.grantAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
                data.commit();

                event.send("nano.authenticated");
                return;
            }
            case ERROR_INVALID_USER: {
                event.send("nano.no_user");
                return;
            }
            case ERROR_HASH_MISMATCH: {
                event.send("nano.incorrect");
            }
        }
    }

    @Achievements(Achievement.NANOWRIMO_AUTHENTICATED)
    @Command(id = "nano.revoke", aliases = {"revoke", "decline"}, help = "nano.revoke.h")
    public String revoke(JDACEvent event) {
        long id = event.asMessageRecieved().getAuthor().getIdLong();

        UserData data = UserData.query(id);
        data.setNanoLink(null);
        data.revokeAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
        data.commit();

        return scripts.get("nano.revoked");
    }

    @Command(id = "nano.info", aliases = {"get", "info"}, help = "nano.info.h")
    @Param(id = "common.username", help = "nano.info.p.name.h")
    public void getUser(JDACEvent event, @Size(min = 1, max = 60) String name) {
        nanowrimo.getUser(name, true).queue((user) -> {
            if (user.getError() == null) {
                event.send(user);
                return;
            }

            switch (user.getError()) {
                case USER_DOES_NOT_EXIST: {
                    event.send("nano.no_user");
                    return;
                }
                case USER_DOES_NOT_HAVE_A_CURRENT_NOVEL: {
                    event.send("nano.info.no_novel");
                }
            }
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
