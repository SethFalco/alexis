package com.elypia.alexis.commandler.validators;

import com.elypia.alexis.entities.UserData;
import com.elypia.alexis.entities.data.Achievement;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;

import java.util.*;

public class AchievementsValidator implements IJDACommandValidator<Achievements> {

    @Override
    public boolean validate(JDACommand event, Achievements annotation) {
        if (!BotUtils.isDatabaseAlive())
            return false;

        long id = event.getMessage().getAuthor().getIdLong();
        UserData data = UserData.query(id);

        Set<Achievement> owned = data.getAchievements();
        List<Achievement> required = new ArrayList<>(Arrays.asList(annotation.value()));

        if (!annotation.invert()) {
            if (!owned.containsAll(required)) {
                required.removeAll(owned);
                return invalidate(event, required, "You require the following achievements to perform this command: ");
            }
        } else {
            if (owned.containsAll(required))
                return invalidate(event, required, "You can't perform this command if you already have the achievements: ");
        }

        return true;
    }

    private boolean invalidate(CommandEvent event, List<Achievement> required, String message) {
        StringJoiner joiner = new StringJoiner(", ");

        for (Achievement achievement : required)
            joiner.add(achievement.getName());

        return false;
    }

    @Override
    public String help(Achievements annotation) {
        return null;
    }
}
