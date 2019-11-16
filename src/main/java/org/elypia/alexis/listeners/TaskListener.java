/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.listeners;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.ChatBot;
import org.elypia.alexis.entities.ActivityData;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.alexis.utils.DatabaseUtils;
import org.hibernate.Session;
import org.slf4j.*;

import javax.inject.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Performs all tasks runtime generic runtime tasks such
 * as logging when the application is ready, or changing activity.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class TaskListener extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(TaskListener.class);

	/** The number of seconds between changing the bots actitity. */
	private static final int ACTIVITY_CHANGE_INTERVAL = 60;

	/** The default {@link Activity} if the the connection to the database is disabled or down. */
	private static final Activity NO_DB_ACTIVITY = Activity.watching("you do commands.");

	/** Connect to the database to update {@link Activity}. */
	private final DatabaseService dbService;

	@Inject
	public TaskListener(final DatabaseService dbService) {
		this.dbService = dbService;
	}

	/**
	 * Log the time taken to startup and set the online status to {@link OnlineStatus#ONLINE}.
	 *
	 * @param event ReadyEvent
	 */
	@Override
	public void onReady(ReadyEvent event) {
		JDA jda = event.getJDA();
		jda.getPresence().setStatus(OnlineStatus.ONLINE);

		long timeElapsed = System.currentTimeMillis() - ChatBot.START_TIME;
		String timeElapsedText = String.format("%,d", timeElapsed);
		logger.info("Time taken to launch: {}ms", timeElapsedText);

		if (dbService.isDisabled()) {
			jda.getPresence().setActivity(NO_DB_ACTIVITY);
			logger.warn("Database is disabled, won't cycle Discord statuses past default.");
		} else {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleWithFixedDelay(() -> {
				try (Session session = dbService.open()) {
					List<ActivityData> activities = DatabaseUtils.getAllRows(ActivityData.class, session);
					ThreadLocalRandom rand = ThreadLocalRandom.current();
					ActivityData data = activities.get(rand.nextInt(activities.size()));
					Activity.ActivityType type = Activity.ActivityType.fromKey(data.getType());
					jda.getPresence().setActivity(Activity.of(type, data.getText(), data.getUrl()));
				}
			}, 0, ACTIVITY_CHANGE_INTERVAL, TimeUnit.SECONDS);

			logger.info("Registered scheduled task to change activity every minute.");
		}
	}
}
