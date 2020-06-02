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
import org.elypia.alexis.persistence.entities.ActivityData;
import org.elypia.alexis.persistence.repositories.ActivityRepository;
import org.quartz.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Schedules task to change the bots displayed {@link Activity}.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@Scheduled(cronExpression = "0 0/1 * 1/1 * ? *", description = "Iterate the activities to display on Discord.")
public class ActivityJob implements Job {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(ActivityJob.class);

    /**
     * Acts as a marker for {@link #previousActivityId} that it can just get the first
     * enabled {@link ActivityData} if one exists.
     */
    private static final int QUERY_ANY_ACTIVITY = -1;

    /** The Discord client to set the status of. */
    private final JDA jda;

    /** Connect to the database to update {@link Activity}. */
    private final ActivityRepository activityRepo;

    /** The last ID that the bot user's {@link Activity} was set to. */
    private int previousActivityId;

    @Inject
    public ActivityJob(final JDA jda, final ActivityRepository activityRepo) {
        this.jda = Objects.requireNonNull(jda);
        this.activityRepo = Objects.requireNonNull(activityRepo);
        previousActivityId = QUERY_ANY_ACTIVITY;
    }

    /**
     * @param context Quartz context.
     */
    @Override
    public void execute(JobExecutionContext context) {
        Optional<ActivityData> optActivityDate = Optional.empty();

        if (previousActivityId != QUERY_ANY_ACTIVITY)
            optActivityDate = activityRepo.findAnyByEnabledTrueAndIdGreaterThan(previousActivityId);

        if (optActivityDate.isEmpty())
            optActivityDate = activityRepo.findAnyByEnabledTrue();

        optActivityDate.ifPresentOrElse((activityData) -> {
            if (activityData.getId() == previousActivityId)
                return;

            Activity.ActivityType type = Activity.ActivityType.fromKey(activityData.getType());
            Activity activity = Activity.of(type, activityData.getText(), activityData.getUrl());

            logger.debug("Setting activity to {}.", activity);
            jda.getPresence().setActivity(activity);

            previousActivityId = activityData.getId();
        }, () -> logger.debug("Not changing bot activity, no applicable activities to change to."));
    }
}
