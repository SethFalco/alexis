/*
 * Copyright 2019-2020 Seth Falco and Alexis Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.modules;

import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.core.ExitCode;
import org.elypia.alexis.core.i18n.AlexisMessages;
import org.elypia.comcord.Scope;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.BotOwner;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.stereotypes.Controller;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.alexis.core.persistence.entities.ActivityData;
import org.elypia.alexis.core.persistence.repositories.ActivityRepository;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Controller(hidden = true)
@StandardController
public class DevController {

    private static final Logger logger = LoggerFactory.getLogger(DevController.class);

    private final ActivityRepository activityRepo;
    private final AlexisMessages messages;

    @Inject
    public DevController(final ActivityRepository activityRepo, AlexisMessages messages) {
        this.activityRepo = Objects.requireNonNull(activityRepo);
        this.messages = Objects.requireNonNull(messages);
    }

    @StandardCommand
    public String listGuilds(@BotOwner Message message) {
        StringJoiner joiner = new StringJoiner("\n");
        Collection<Guild> guilds = message.getJDA().getGuilds();

        for (Guild guild : guilds)
           joiner.add(messages.devGuildInfo(guild.getId(), guild.getName(), guild.getMembers().size()));

        return joiner.toString();
    }

    @StandardCommand
    public String leaveGuild(@BotOwner Message message, @Param @Scoped(inPrivate = Scope.GLOBAL) Guild guild) {
        guild.leave().queue();
        return messages.devLeftGuild();
    }

    @StandardCommand
    public String setUsername(@BotOwner Message message, @Param String input) {
        SelfUser self = message.getJDA().getSelfUser();

        if (self.getName().equals(input))
            return messages.devRenameNoChange(input);

        self.getManager().setName(input).queue();
        return messages.devChangedBotsName(input);
    }

    @StandardCommand
    public String setAvatar(@BotOwner Message message, @Param URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);
            message.getJDA().getSelfUser().getManager().setAvatar(icon).queue();
            return messages.devChangedBotsAvatar();
        }
    }

    /**
     * @param message The message that triggered this event.
     */
    @StandardCommand
    public void shutdown(@BotOwner Message message) {
        message.getJDA().shutdown();
        logger.info("Logged out of Discord.");

        Commandler.stop();
        logger.info("Finished shutting down Commandler, now shutting down application.");

        System.exit(ExitCode.NORMAL.getId());
    }

    @StandardCommand
    public String addActivity(@BotOwner Message message, @Param Activity.ActivityType type, @Param String body) {
        ActivityData data = new ActivityData(type.getKey(), body);
        activityRepo.save(data);
        return messages.devAddedActivity();
    }

    @StandardCommand
    public String removeActivity(@BotOwner Message message, @Param int activityId) {
        Optional<ActivityData> optActivity = activityRepo.findOptionalBy(activityId);

        if (optActivity.isEmpty())
            return messages.devActivityDoesNotExist(activityId);

        ActivityData activity = optActivity.get();
        activityRepo.attachAndRemove(activity);
        return messages.devRemovedActivity(activity.getText());
    }

    @StandardCommand
    public String listActivities(@BotOwner Message message) {
        List<ActivityData> activities = activityRepo.findAll();
        StringJoiner joiner = new StringJoiner("\n");

        for (ActivityData activity : activities)
            joiner.add(activity.getId() + " " + Activity.ActivityType.fromKey(activity.getType()) + " " + activity.getText());

        return joiner.toString();
    }
}
