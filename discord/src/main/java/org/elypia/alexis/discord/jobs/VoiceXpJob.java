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
import net.dv8tion.jda.api.entities.*;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.elypia.alexis.repositories.GuildRepository;
import org.quartz.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Schedules task to change the bots displayed {@link Activity}.
 * If the connection to {@link DatabaseService} is severred, it'll
 * default to skip until the connection can is restablished.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@Scheduled(cronExpression = "0 0/1 * 1/1 * ? *", description = "Iterate the activities to display on Discord.")
public class VoiceXpJob implements Job {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(VoiceXpJob.class);

    /** Base XP user may be awarded per interval. */
    private static final int BASE_XP = 8;

    /** The Discord client to set the status of. */
    private final JDA jda;

    private final GuildRepository guildRepo;

    @Inject
    public VoiceXpJob(final JDA jda, final GuildRepository guildRepo) {
        this.jda = Objects.requireNonNull(jda);
        this.guildRepo = Objects.requireNonNull(guildRepo);
    }

    /**
     * Per interval, reward all users in voice channels with XP.
     * If a user is in a suppressed channel, or deafened, they will
     * not receive any XP.
     *
     * @param context Quartz context.
     */
    @Override
    public void execute(JobExecutionContext context) {
        Collection<Guild> guilds = jda.getGuilds();
        int totalXpAwarded = 0;

        for (Guild guild : guilds) {
            Collection<VoiceChannel> channels = guild.getVoiceChannels();
            int guildXpAwarded = 0;

            for (VoiceChannel channel : channels) {
                Collection<Member> members = channel.getMembers();

                for (Member member : members) {
                    GuildVoiceState voiceStates = member.getVoiceState();
                    int xpAwarded = calculateXpAward(voiceStates);
                    guildXpAwarded += xpAwarded;
                }
            }

            if (guildXpAwarded != 0) {
                totalXpAwarded += guildXpAwarded;
                guildRepo.incrementXp(guildXpAwarded, guild.getIdLong());
            }
        }

        if (totalXpAwarded > 0)
            logger.debug("Awarded a total of {} XP this interval.", totalXpAwarded);
    }

    private int calculateXpAward(GuildVoiceState states) {
        if (states.isSuppressed() || states.isDeafened())
            return 0;

        int xpAwarded = BASE_XP;

        if (states.isStream())
            xpAwarded *= 2;

        if (states.isMuted())
            xpAwarded /= 2;

        return xpAwarded;
    }
}
