/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.alexis.discord.jobs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.elypia.alexis.entities.ActivityData;
import org.elypia.alexis.repositories.ActivityRepository;
import org.quartz.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

// TODO: Make Alexis run without database
/**
 * Schedules task to change the bots displayed {@link Activity}.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@Scheduled(cronExpression = "0 0/1 * 1/1 * ? *", description = "Iterate the activities to display on Discord.")
public class ActivityJob implements Job {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(ActivityJob.class);

    /** The Discord client to set the status of. */
    private final JDA jda;

    /** Connect to the database to update {@link Activity}. */
    private final ActivityRepository activityRepo;

    private int previousActivityId;

    @Inject
    public ActivityJob(final JDA jda, final ActivityRepository activityRepo) {
        this.jda = Objects.requireNonNull(jda);
        this.activityRepo = Objects.requireNonNull(activityRepo);
    }

    /**
     * TODO: Only selects first two items
     * @param context Quartz context.
     */
    @Override
    public void execute(JobExecutionContext context) {
        Optional<ActivityData> optActivityDate;

        if (previousActivityId != 0)
            optActivityDate = activityRepo.findAnyByEnabledTrueAndIdNotEqual(previousActivityId);
        else
            optActivityDate = activityRepo.findAnyByEnabledTrue();

        if (optActivityDate.isEmpty()) {
            logger.info("Not changing bot activity, no other activities to change to.");
            return;
        }

        ActivityData activityData = optActivityDate.get();
        previousActivityId = activityData.getId();

        Activity.ActivityType type = Activity.ActivityType.fromKey(activityData.getType());
        Activity activity = Activity.of(type, activityData.getText(), activityData.getUrl());

        logger.debug("Setting activity to {}.", activity);
        jda.getPresence().setActivity(activity);
    }
}
