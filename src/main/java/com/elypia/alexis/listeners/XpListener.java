package com.elypia.alexis.listeners;

import com.elypia.alexis.database.DatabaseService;
import com.elypia.alexis.database.entities.*;
import com.elypia.elypiai.runescape.RuneScape;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.*;

import java.util.*;

public class XpListener extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(XpListener.class);

    private final DatabaseService dbService;

    public XpListener(final DatabaseService dbService) {
        this.dbService = dbService;

        if
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!dbService.isEnabled())
            return;

        if (!event.getChannelType().isGuild() || event.getAuthor().isBot())
            return;

        if (event.getGuild().getMembers().stream().filter(o -> !o.getUser().isBot()).count() == 1)
            return;

        long userId = event.getAuthor().getIdLong();

        UserData userData = UserData.query(userId);

        if (!userData.isEligibleForXp(event)) {
            userData.setLastMessage(new Date());
            userData.commit();
            return;
        }

        userData.setLastMessage(new Date());

        long guildId = event.getGuild().getIdLong();
        GuildData guildData = GuildData.query(guildId);
        GuildSettings settings = guildData.getSettings();

        MemberData memberData = MemberData.query(userId, guildId);

        int reward = userData.calculateXp(event);

        userData.incrementXp(reward);
        userData.commit();

        guildData.incrementXp(reward);
        guildData.commit();

        int currentLevel = RuneScape.parseXpAsLevel(memberData.getXp());
        int newLevel = RuneScape.parseXpAsLevel(memberData.incrementXp(reward));

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

        memberData.commit();
    }
}


