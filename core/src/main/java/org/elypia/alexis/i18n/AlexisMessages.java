package org.elypia.alexis.i18n;

import com.google.api.client.util.DateTime;
import org.apache.deltaspike.core.api.message.*;

import java.time.OffsetDateTime;

/**
 * Resource bundle messages for things Alexis will say in runtime.
 * This doesn't include validation messages, or commandler component metadata.
 */
@MessageBundle
@MessageContextConfig(localeResolver = AlexisLocaleResolver.class)
public interface AlexisMessages {

    @MessageTemplate("ID: ")
    String uniqueIdentifier();

    @MessageTemplate("The global level notification feature is enabled but no message is set, disabling global level notifications.")
    String auditLevelEnabledNoMessage();

    @MessageTemplate("Well done you went from level %,d to level %,d!")
    String userLeveledUp(int oldLevel, int newLevel);

    @MessageTemplate("Thank you for inviting me! My default prefix is `$` but you can mention me too!\nFeel free to try my help command!")
    String thankYouForInvite();

    @MessageTemplate("Author")
    String botAuthor();

    @MessageTemplate("Total Guilds")
    String botTotalGuilds();

    @MessageTemplate("Total Users (Bots)")
    String botTotalUsers();

    @MessageTemplate("Our Guild")
    String botSupportGuild();

    @MessageTemplate("No definitions were found.")
    String udNoDefinitions();

    @MessageTemplate("Example")
    String udExampleUsageOfWord();

    @MessageTemplate("Scores")
    String udThumbsUpThumbsDown();

    @MessageTemplate("Results for %,d words.")
    String udTotalResults(int total);

    @MessageTemplate("Total Playtime")
    String steamTotalPlaytime();

    @MessageTemplate("Recent Playtime")
    String steamRecentPlaytime();

    @MessageTemplate("Hours")
    String steamPlaytimeHours();

    @MessageTemplate("Username")
    String osuUsername();

    @MessageTemplate("Level")
    String osuLevel();

    @MessageTemplate("Ranked Score")
    String osuRankedScore();

    @MessageTemplate("Total Score")
    String osuTotalScore();

    @MessageTemplate("Performance Points")
    String osuPp();

    @MessageTemplate("Rank (Country)")
    String osuRank();

    @MessageTemplate("Accuracy")
    String osuAccuracy();

    @MessageTemplate("Play Count")
    String osuPlayCount();

    @MessageTemplate("Latest Activity")
    String osuLatestActivity();

    @MessageTemplate("You can get more information from %s.")
    String steamMoreInfo(String url);

    @MessageTemplate("Last Log Off")
    String steamLastLogOff();

    @MessageTemplate("Time Created")
    String steamTimeAccountCreated();

    @MessageTemplate("Currently Playing")
    String steamCurrentlyPlaying();

    @MessageTemplate("Steam ID")
    String steamId();

    @MessageTemplate("Heads")
    String coinFlipHeads();

    @MessageTemplate("Tails")
    String coinClipTails();

    @MessageTemplate("Sorry, I couldn't find that user on Steam. If it helps, you should give the ID, or what would be at the end of their custom URL.")
    String steamUserNotFound();

    @MessageTemplate("I found the user, but I was unable to access their profile.")
    String steamProfilePrivate();

    @MessageTemplate("I found the user, but I was unable to access their library.")
    String steamLibraryPrivate();

    @MessageTemplate("Source")
    String translateSource();

    @MessageTemplate("Target")
    String translateTarget();

    @MessageTemplate("Total Views")
    String twitchTotalViews();

    @MessageTemplate("Type")
    String twitchType();

    @MessageTemplate("Joined %s")
    String userJoinedGuild(String guildName);

    @MessageTemplate("Joined Discord")
    String userJoinedDiscord();

    @MessageTemplate("Bot")
    String userBot();

    @MessageTemplate("%tF\n%,d days ago")
    String userJoinAge(OffsetDateTime date, long days);

    @MessageTemplate("Invite Link")
    String botInviteLink();

    @MessageTemplate("I'm struggling to connect to YouTube at the moment, could you try again later!")
    String youtubeApiError();

    @MessageTemplate("There were no results for that.")
    String noSearchResultsFound();

    @MessageTemplate("Published on %tF")
    String youtubePublishedOn(DateTime date);

    @MessageTemplate("ping!")
    String ping();

    @MessageTemplate("pong!")
    String pong();

    @MessageTemplate("Invite %s")
    String inviteBot(String botName);

    @MessageTemplate("This guild has no skills with the name `%s`.")
    String noSkillsWithThatName(String skillName);

    @MessageTemplate("I've assigned the skill `%s` to the text channel %s.")
    String assignedSkillToTextChannel(String skillName, String channelName);

    @MessageTemplate("The prefix has been changed to %s.")
    String prefixHasBeenChanged(String newPrefix);

    @MessageTemplate("I'll now only respond to mentions.")
    String disablePrefixMentionsOnly();

    @MessageTemplate("Feel free to give it a try whenever you're ready, it's been enabled for the guild.")
    String featureEnabledTryItNow();

    @MessageTemplate("Understood; I'll stop translating messages on reaction now.")
    String reactionFeatureDisabled();
}
