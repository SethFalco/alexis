package com.elypia.alexis.listeners;

import com.elypia.alexis.entities.*;
import com.elypia.alexis.services.DatabaseService;
import com.elypia.elypiai.runescape.RuneScape;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hibernate.Session;
import org.slf4j.*;

import java.time.Duration;
import java.util.*;

/**
 * Handles calcuating and giving XP to users.
 */
public class XpListener extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(XpListener.class);

    /** Current highest WPM. */
    private static final int FASTEST_WPM = 212;

    /** The average length of an English word. */
    private static final int AVERAGE_WORD_LENGTH = 5;

    /** The max allowed characters per second before Alexis considers the message too fast. */
    private static final int ALLOWED_CPS = FASTEST_WPM * AVERAGE_WORD_LENGTH / 60;

    /** MAX XP per Message, + 1 for whitespace. */
    private static final int MAX_XP_PM = Message.MAX_CONTENT_LENGTH / AVERAGE_WORD_LENGTH;

    private final DatabaseService dbService;

    public XpListener(final DatabaseService dbService) {
        this.dbService = dbService;

        if (!dbService.isEnabled())
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

            userData.setXp(userData.getXp() + xp);
            memberData.setXp(memberData.getXp() + xp);
            guildData.setXp(guildData.getXp() + xp);

            GuildSettings settings = guildData.getSettings();

            int currentLevel = RuneScape.parseXpAsLevel(memberData.getXp());
            int newLevel = RuneScape.parseXpAsLevel(memberData.getXp());

            if (currentLevel != newLevel) {
                boolean enabled = settings.getLevelSettings().getNotifySettings().isEnabled();

                if (enabled)
                    event.getChannel().sendMessage("Well done you went from level " + currentLevel + " to level " + newLevel + "!").queue();
            }

            List<SkillEntry> skills = settings.getSkills();
            List<MemberSkill> memberSkills = memberData.getSkills();

            skills.forEach((skill) -> {
                if (skill.getChannels().contains(event.getChannel().getIdLong())) {
                    if (memberSkills.stream().noneMatch(o -> o.getName().equalsIgnoreCase(skill.getName())))
                        memberSkills.add(new MemberSkill(skill.getName()));

                    for (MemberSkill mSkill : memberSkills) {
                        if (mSkill.getName().equalsIgnoreCase(skill.getName())) {
                            int skillLevel = RuneScape.parseXpAsLevel(mSkill.getXp());
                            int newSkillLevel = RuneScape.parseXpAsLevel(mSkill.incrementXp(reward));

                            if (skillLevel != newSkillLevel && skill.isNotify()) {
                                var params = Map.of("skill", skill.getName(), "level", newSkillLevel);
//							event.getChannel().sendMessage(BotUtils.get("skill.level_up", event, params)).queue();
                            }

                            return;
                        }
                    }
                }
            });

            session.getTransaction().commit();
        }
    }

    /**
     * @param event The event that occured.
     * @return If this event is allowed eligable to grant XP to entities.
     */
    private boolean isValid(MessageReceivedEvent event) {
        if (!dbService.isEnabled())
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

    public int calculateLevelFromXp() {
        return 0;
    }
}
