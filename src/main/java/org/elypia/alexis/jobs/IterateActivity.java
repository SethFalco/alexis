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

package org.elypia.alexis.jobs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.elypia.alexis.entities.ActivityData;
import org.elypia.alexis.repositories.ActivityRepository;
import org.elypia.alexis.services.DatabaseService;
import org.slf4j.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Schedules task to change the bots displayed {@link Activity}.
 * If the connection to {@link DatabaseService} is severred, it'll
 * default to skip until the connection can is restablished.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Scheduled(cronExpression = "* * * * *", description = "Iterate the activities to display on Discord.")
public class IterateActivity implements Runnable {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(IterateActivity.class);

    /** Connect to the database to update {@link Activity}. */
    private final ActivityRepository activityRepo;

    /** The Discord client to set the status of. */
    private final JDA jda;

    @Inject
    public IterateActivity(final ActivityRepository activityRepo, final JDA jda) {
        this.activityRepo = activityRepo;
        this.jda = jda;
    }

    @Override
    public void run() {
        List<ActivityData> activities = activityRepo.findAll();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int count = activities.size();

        if (count < 1)
            return;

        int index = rand.nextInt(count);
        ActivityData activityData = activities.get(index);
        Activity.ActivityType type = Activity.ActivityType.fromKey(activityData.getType());
        Activity activity = Activity.of(type, activityData.getText(), activityData.getUrl());

        jda.getPresence().setActivity(activity);
    }
}
