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

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.repositories.*;
import org.elypia.alexis.services.AuditService;
import org.elypia.alexis.utils.*;
import org.slf4j.*;

import javax.inject.*;
import java.time.Duration;
import java.util.*;

/**
 * Handles calculating and granting XP (experience) to users.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class XpListener extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(XpListener.class);

    /**
     *  Max rewardable words per minute.
     *
     *  If you type faster than this, you're a fire hazard.
     */
    private static final int FASTEST_WPM = 300;

    /** The average length of an English word. */
    private static final int AVERAGE_WORD_LENGTH = 5;

    /** The max allowed characters per second before ChatBot considers the message too fast. */
    private static final int ALLOWED_CPS = FASTEST_WPM * AVERAGE_WORD_LENGTH / 60;

    /** MAX XP per Message, + 1 for whitespace. */
    private static final int MAX_XP_PM = Message.MAX_CONTENT_LENGTH / AVERAGE_WORD_LENGTH;

    private final GuildRepository guildRepo;
    private final UserRepository userRepo;
    private final AuditService auditService;

    @Inject
    public XpListener(final GuildRepository guildRepo, final UserRepository userRepo, final AuditService auditService) {
        this.guildRepo = Objects.requireNonNull(guildRepo);
        this.userRepo = Objects.requireNonNull(userRepo);
        this.auditService = Objects.requireNonNull(auditService);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isValid(event))
            return;

        long userId = event.getAuthor().getIdLong();
        String content = event.getMessage().getContentDisplay().trim();

//        try (Session session = dbService.open()) {
//            UserData userData = session.get(UserData.class, userId);
//            Date lastMessage = userData.getLastMessage();
//            userData.setLastMessage(new Date());
//
//            if (!isTypingTooFast(content.length(), lastMessage))
//                return;
//
//            int xp = content.split("\\s+", MAX_XP_PM).length;
//
//            long guildId = event.getGuild().getIdLong();
//            GuildData guildData = session.get(GuildData.class, guildId);
//
//            MemberData memberData = session.get(MemberData.class, null);
//
//            memberData.setXp(memberData.getXp() + xp);
//            guildData.setXp(guildData.getXp() + xp);
//
//            GuildFeature feature = null;
//
//
//            session.getTransaction().commit();
//        }
    }

    /**
     * @param event
     * @param userData
     * @param guildData
     * @param earned
     */
    private void giveUserXp(MessageReceivedEvent event, UserData userData, GuildData guildData, int earned) {
        int currentLevel = LevelUtils.getLevelFromXp(userData.getXp());
        userData.setXp(userData.getXp() + earned);
        int newLevel = LevelUtils.getLevelFromXp(userData.getXp());

        if (currentLevel != newLevel) {
            GuildFeature feature = null;

            if (feature.isEnabled())
                event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
        }
    }

    private void getMemberXp(MessageReceivedEvent event, MemberData memberData, GuildData guildData, int earned) {
        int currentLevel = LevelUtils.getLevelFromXp(memberData.getXp());
        memberData.setXp(memberData.getXp() + earned);
        int newLevel = LevelUtils.getLevelFromXp(memberData.getXp());

        if (currentLevel != newLevel) {
            GuildFeature feature = null;

            if (feature.isEnabled())
                event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
        }
    }

    /**
     * @param event The event that allowed the guild to gain XP.
     * @param guildData The guild data in the database.
     * @param earned The amount of XP the guild earned in this event.
     */
    private void giveGuildXp(GenericMessageEvent event, GuildData guildData, int earned) {
        long oldXp = guildData.getXp();
        float multipler = guildData.getMutlipler();

        int oldLevel = LevelUtils.getLevelFromXp(oldXp, multipler);
        guildData.setXp(oldXp += earned);
        int newLevel = LevelUtils.getLevelFromXp(oldXp, multipler);

        if (oldLevel == newLevel)
            return; // Didn't level up.

        GuildFeature feature = null;

        if (!feature.isEnabled())
            return; // Notifications not enabled.

        Guild guild = event.getGuild();
        MessageChannel channel = DiscordUtils.getWriteableChannel(guild);

        String message = "Random thing";

        if (message == null) {
            logger.info("Guild leveled up with notifications enabled but no message configured.");
            auditService.log(guild, guildData, "The global level notification feature is enabled but no message is set, disabling global level notifications.");
            feature.setEnabled(false);
            return;
        }

        channel.sendMessage(message).queue();
    }

    private void giveSkillsXp(MessageReceivedEvent event, UserData userData, GuildData guildData, int earned) {
        int currentLevel = LevelUtils.getLevelFromXp(userData.getXp());
        userData.setXp(userData.getXp() + earned);
        int newLevel = LevelUtils.getLevelFromXp(userData.getXp());

        if (currentLevel != newLevel) {
            GuildFeature feature = null;

            if (feature.isEnabled())
                event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
        }
    }

    /**
     * @param event The event that occured.
     * @return If this event is allowed eligable to grant XP to entities.
     */
    private boolean isValid(MessageReceivedEvent event) {
        if (!event.getChannelType().isGuild() || event.getAuthor().isBot())
            return false;

        return event.getGuild().getMembers().stream()
            .filter(o -> !o.getUser().isBot())
            .count() == 1;
    }

    /**
     * This has been seperated from {@link #isValid(MessageReceivedEvent)}
     * to avoid having to query for {@link UserData} if it's already been ruled out
     * as required before by checks which don't require database interaction.
     *
     * @return If the user is deemed typing too fast to deserve XP.
     */
    private boolean isTypingTooFast(int chars, Date lastMessage) {
        if (lastMessage == null)
            return false;

        long current = System.currentTimeMillis();
        long previous = lastMessage.getTime();

        Duration timePassed = Duration.ofMillis(current - previous);
        long seconds = timePassed.toSeconds();
        long cps = chars / seconds;

        return cps <= ALLOWED_CPS;
    }
}
