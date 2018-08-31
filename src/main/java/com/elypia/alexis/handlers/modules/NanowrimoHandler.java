package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.*;
import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.entities.data.Achievement;
import com.elypia.alexis.entities.embedded.NanowrimoLink;
import com.elypia.alexis.utils.DiscordLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Secret;
import com.elypia.elypiai.nanowrimo.Nanowrimo;
import com.elypia.elyscript.ElyScript;

import java.io.IOException;

@Module(name = "National Novel Writing Month", aliases = {"nanowrimo", "nano", "nnwm"}, help = "NaNoWriMo commands for authenticating and viewing other writers.")
public class NanowrimoHandler extends JDAHandler {

    private static final ElyScript NO_NANO_USER = new ElyScript("I('m sorry| apologise) (however|but) I (was(n't | un)able to|(did|could)(n't| not)) (find|get|obtain) a user (under|with) th(at name|e name you specified.)( :c){?}");

    private Nanowrimo nanowrimo;

    public NanowrimoHandler() {
        nanowrimo = new Nanowrimo();
    }

    @Secret
    @Database
    @Achievements(value = Achievement.NANOWRIMO_AUTHENTICATED, invert = true)
    @Command(name = "Authenticate to NaNoWriMo", aliases = {"authenticate", "auth"}, help = "Auth to your NaNoWriMo account. Remember NOT to share your secret!")
    @Param(name = "name", help = "Your NaNoWriMo username.")
    @Param(name = "secret", help = "Your NaNoWriMo secret at: https://nanowrimo.org/api/wordcount")
    @Param(name = "wordcount", help = "Your total word count to submit.")
    public void authenticate(JDACommand event, @Length(min = 1, max = 60) String name, String secret, @Limit(min = 0) int wordcount) throws IOException {
        switch (nanowrimo.setWordCount(secret, name, wordcount)) {
            case ERROR_NO_ACTIVE_EVENT: {
                UserData data = UserData.query(event.getMessage().getAuthor().getIdLong());
                data.setNanoLink(new NanowrimoLink(name, secret, false));
                data.grantAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
                data.commit();

                event.reply("Gratz, you've succesfully authenticated to your NaNoWriMo account!");
                return;
            }
            case ERROR_INVALID_USER: {
                event.reply("Sorry, that user doesn't exist.");
                return;
            }
            case ERROR_HASH_MISMATCH: {
                event.reply("Sorry, either the username or secret you passed were incorrect, care to verify these and try again?");
            }
        }
    }

    @Achievements(Achievement.NANOWRIMO_AUTHENTICATED)
    @Command(name = "Revoke your Authenticated NaNoWriMo Account", aliases = {"revoke", "decline"}, help = "Revoke the authentication to your NaNoWriMo account!")
    public String revoke(JDACommand event) {
        long id = event.getMessage().getAuthor().getIdLong();

        UserData data = UserData.query(id);
        data.setNanoLink(null);
        data.revokeAchievement(Achievement.NANOWRIMO_AUTHENTICATED);
        data.commit();

        return "Authentication has been revoked.";
    }

    @Command(name = "Writer Info", aliases = {"get", "info"}, help = "Get basic information on a user.")
    @Param(name = "name", help = "Your NaNoWriMo username.")
    public void getUser(JDACommand event, @Length(min = 1, max = 60) String name) {
        nanowrimo.getUser(name, true).queue((user) -> {
            if (user.getError() == null) {
                event.reply(user);
                return;
            }

            switch (user.getError()) {
                case USER_DOES_NOT_EXIST: {
                    event.reply("Sorry, that user doesn't exist.");
                    return;
                }
                case USER_DOES_NOT_HAVE_A_CURRENT_NOVEL: {
                    event.reply("There is no information to display as that user does not have a current novel!");
                }
            }
        }, (ex) -> DiscordLogger.log(event, ex));
    }
}
