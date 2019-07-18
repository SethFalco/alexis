package com.elypia.alexis.listeners;

import com.elypia.alexis.entities.*;
import com.elypia.alexis.services.*;
import com.elypia.alexis.utils.LevelUtils;
import com.google.inject.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hibernate.Session;
import org.slf4j.*;

import java.time.Duration;
import java.util.*;

/** Handles calcuating and giving XP to users. */
@Singleton
public class XpListener extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(XpListener.class);

    /** Current highest WPM. */
    private static final int FASTEST_WPM = 212;

    /** The average length of an English word. */
    private static final int AVERAGE_WORD_LENGTH = 5;

    /** The max allowed characters per second before ChatBot considers the message too fast. */
    private static final int ALLOWED_CPS = FASTEST_WPM * AVERAGE_WORD_LENGTH / 60;

    /** MAX XP per Message, + 1 for whitespace. */
    private static final int MAX_XP_PM = Message.MAX_CONTENT_LENGTH / AVERAGE_WORD_LENGTH;

    private final DatabaseService dbService;
    private final AuditService auditService;

    @Inject
    public XpListener(final DatabaseService dbService, final AuditService auditService) {
        this.dbService = Objects.requireNonNull(dbService);
        this.auditService = Objects.requireNonNull(auditService);

        if (dbService.isDisabled())
            logger.info("Database is disabled so XP will not be rewarded.");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isValid(event))
            return;

        long userId = event.getAuthor().getIdLong();
        String content = event.getMessage().getContentDisplay().trim();

        try (Session session = dbService.open()) {
            UserData userData = session.get(UserData.class, userId);
            Date lastMessage = userData.getLastMessage();
            userData.setLastMessage(new Date());

            if (!isTypingTooFast(content.length(), lastMessage))
                return;

            int xp = content.split("\\s+", MAX_XP_PM).length;

            long guildId = event.getGuild().getIdLong();
            GuildData guildData = session.get(GuildData.class, guildId);

            MemberData memberData = session.get(MemberData.class, null);

            memberData.setXp(memberData.getXp() + xp);
            guildData.setXp(guildData.getXp() + xp);

            GuildFeature feature = null;


            session.getTransaction().commit();
        }
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
            GuildFeature feature = guildData.getFeature("GLOBAL_LEVEL_NOTIFICATION");

            if (feature.isEnabled()) {

                event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
            }
        }
    }

    private void getMemberXp(MessageReceivedEvent event, MemberData memberData, GuildData guildData, int earned) {
        int currentLevel = LevelUtils.getLevelFromXp(memberData.getXp());
        memberData.setXp(memberData.getXp() + earned);
        int newLevel = LevelUtils.getLevelFromXp(memberData.getXp());

        if (currentLevel != newLevel) {
            GuildFeature feature = guildData.getFeature("GUILD_LEVEL_NOTIFICATION");

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

        GuildFeature feature = guildData.getFeature("GLOBAL_LEVEL_NOTIFICATION");

        if (!feature.isEnabled())
            return; // Notifications not enabled.

        Guild guild = event.getGuild();
        MessageSetting settings = guildData.getMessage(3);
        Long channelId = settings.getChannelId();
        MessageChannel channel = (channelId != null) ? guild.getTextChannelById(channelId) : event.getChannel();

        String message = settings.getMessage();

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
            GuildFeature feature = guildData.getFeature("GLOBAL_LEVEL_NOTIFICATIONS");

            if (feature.isEnabled())
                event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
        }
    }

    /**
     * @param event The event that occured.
     * @return If this event is allowed eligable to grant XP to entities.
     */
    private boolean isValid(MessageReceivedEvent event) {
        if (dbService.isDisabled())
            return false;

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
