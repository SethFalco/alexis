package org.elypia.alexis.i18n;

import org.apache.deltaspike.core.api.message.*;

import java.time.*;
import java.util.Date;

/**
 * Resource bundle messages for things Alexis will say in runtime.
 * This doesn't include validation messages, or commandler component metadata.
 */
@MessageBundle
public interface AlexisMessages {

    @MessageTemplate("{deleted_or_banned}")
    String deletedOrBanned();

    @MessageTemplate("{guild_owner}")
    String guildOwner();

    @MessageTemplate("{command_prefix}")
    String commandPrefix();

    @MessageTemplate("{current_locale}")
    String currentLocale();

    @MessageTemplate("{read_more}")
    String readMore();

    @MessageTemplate("{default_user_join_message}")
    String defaultUserJoinMessage();

    @MessageTemplate("{default_bot_join_message}")
    String defaultBotJoinMessage();

    @MessageTemplate("{default_user_leave_message}")
    String defaultUserLeaveMessage();

    @MessageTemplate("{default_bot_leave_message}")
    String defaultBotLeaveMessage();

    @MessageTemplate("{done}")
    String done();

    @MessageTemplate("{unique_identifier}")
    String uniqueIdentifier(Object id);

    @MessageTemplate("{audit_level_enabled_no_message}")
    String auditLevelEnabledNoMessage();

    @MessageTemplate("{user_leveled_up}")
    String userLeveledUp(int oldLevel, int newLevel);

    @MessageTemplate("{thank_you_for_invite}")
    String thankYouForInvite();

    @MessageTemplate("{request_failed_unknown_reason}")
    String requestFailedUnknownReason();

    @MessageTemplate("{dev_left_guild}")
    String devLeftGuild();

    @MessageTemplate("{dev_rename_no_change}")
    String devRenameNoChange(String name);

    @MessageTemplate("{dev_changed_bots_name}")
    String devChangedBotsName(String newName);

    @MessageTemplate("{dev_changed_bots_avatar}")
    String devChangedBotsAvatar();

    @MessageTemplate("{dev_guild_info}")
    String devGuildInfo(String guildId, String guildName, int memberCount);

    @MessageTemplate("{emote_post_lack_permissions}")
    String emotePostLackPermissions(String emoteUrl);

    @MessageTemplate("{emote_guild_no_emotes}")
    String emoteGuildNoEmotes(String guildName);

    @MessageTemplate("{emote_leaderboard_no_emotes}")
    String emoteLeaderboardNoEmotes();

    @MessageTemplate("{emote_leaderboard_never_used}")
    String emoteLeaderboardNeverUsed();

    @MessageTemplate("{emote_leaderboard_title}")
    String emoteLeaderboardTitle();

    @MessageTemplate("{emote_leaderboard_local}")
    String emoteLeaderboardLocal();

    @MessageTemplate("{emote_leaderboard_global}")
    String emoteLeaderboardGlobal();

    @MessageTemplate("{bot_author}")
    String botAuthor();

    @MessageTemplate("{bot_total_guilds}")
    String botTotalGuilds();

    @MessageTemplate("{total_bots_and_users}")
    String totalBotsAndUsers();

    @MessageTemplate("{bot_support_guild}")
    String botSupportGuild();

    @MessageTemplate("{bot_user_in_support_guild_already}")
    String botUserInSupportGuildAlready();

    @MessageTemplate("{ud_no_definitions}")
    String udNoDefinitions();

    @MessageTemplate("{ud_example_usage_of_word}")
    String udExampleUsageOfWord();

    @MessageTemplate("{ud_thumbs_up_thumbs_down}")
    String udThumbsUpThumbsDown();

    @MessageTemplate("{ud_total_results}")
    String udTotalResults(int total);

    @MessageTemplate("{steam_total_playtime}")
    String steamTotalPlaytime();

    @MessageTemplate("{steam_recent_playtime}")
    String steamRecentPlaytime();

    @MessageTemplate("{steam_playtime_hours}")
    String steamPlaytimeHours();

    @MessageTemplate("{player_not_found}")
    String playerNotFound(String username);

    @MessageTemplate("{osu_username}")
    String osuUsername();

    @MessageTemplate("{user_level}")
    String userLevel();

    @MessageTemplate("{osu_ranked_score}")
    String osuRankedScore();

    @MessageTemplate("{osu_total_score}")
    String osuTotalScore();

    @MessageTemplate("{osu_pp}")
    String osuPp();

    @MessageTemplate("{osu_rank}")
    String osuRank();

    @MessageTemplate("{osu_accuracy}")
    String osuAccuracy();

    @MessageTemplate("{osu_play_count}")
    String osuPlayCount();

    @MessageTemplate("{osu_latest_activity}")
    String osuLatestActivity();

    @MessageTemplate("{osu_player_not_found}")
    String osuPlayerNotFound();

    @MessageTemplate("{steam_more_info}")
    String steamMoreInfo(String url);

    @MessageTemplate("{steam_last_log_off}")
    String steamLastLogOff();

    @MessageTemplate("{steam_time_account_created}")
    String steamTimeAccountCreated();

    @MessageTemplate("{steam_currently_playing}")
    String steamCurrentlyPlaying();

    @MessageTemplate("{steam_id}")
    String steamId(long steamId);

    @MessageTemplate("{coin_flip_heads}")
    String coinFlipHeads();

    @MessageTemplate("{coin_clip_tails}")
    String coinClipTails();

    @MessageTemplate("{steam_user_not_found}")
    String steamUserNotFound();

    @MessageTemplate("{steam_return_steam64_id}")
    String steamReturnSteam64Id(String username, long id);

    @MessageTemplate("{steam_profile_private}")
    String steamProfilePrivate();

    @MessageTemplate("{steam_library_private}")
    String steamLibraryPrivate();

    @MessageTemplate("{translate_source}")
    String translateSource();

    @MessageTemplate("{translate_target}")
    String translateTarget();

    @MessageTemplate("{translate_no_last_message}")
    String translateNoLastMessage();

    @MessageTemplate("{translate_cant_translate_embeds}")
    String translateCantTranslateEmbeds();

    @MessageTemplate("{twitch_total_views}")
    String twitchTotalViews();

    @MessageTemplate("{twitch_type}")
    String twitchType();

    @MessageTemplate("{twitch_user_not_found}")
    String twitchUserNotFound();

    @MessageTemplate("{greeting_send_messages_to}")
    String greetingSendMessagesTo(String messageType, String channelMention);

    @MessageTemplate("{greeting_set_new_message}")
    String greetingSetNewMessage(String messageType, String message);

    @MessageTemplate("{greeting_update_existing_message}")
    String greetingUpdateExistingMessage(String messageType, String newMessage);

    @MessageTemplate("{greeting_message_not_changed}")
    String greetingMessageNotChanged(String messageType);

    @MessageTemplate("{greeting_channel_not_set_so_defaulting}")
    String greetingChannelNotSetSoDefaulting(String messageType, String channelMention);

    @MessageTemplate("{greeting_channel_set_but_deleted}")
    String greetingChannelSetButDeleted(String messageType, String channelMention);

    @MessageTemplate("{greeting_toggled_feature}")
    String greetingToggledFeature(String feature);

    @MessageTemplate("{greeting_modifed_already_by_anon_user}")
    String greetingModifedAlreadyByAnonUser(String feature);

    @MessageTemplate("{greeting_modified_already}")
    String greetingModifiedAlready(String feature, Date date, String memberName);

    @MessageTemplate("{greeting_updated_feature}")
    String greetingUpdatedFeature(String feature);

    @MessageTemplate("{user_joined_guild}")
    String userJoinedGuild(String guildName);

    @MessageTemplate("{user_joined_discord}")
    String userJoinedDiscord();

    @MessageTemplate("{user_bot}")
    String userBot();

    @MessageTemplate("{user_join_age}")
    String userJoinAge(OffsetDateTime date, long days);

    @MessageTemplate("{bot_invite_link}")
    String botInviteLink();

    @MessageTemplate("{bot_description}")
    String botDescription(String description, String inviteLink);

    @MessageTemplate("{youtube_api_error}")
    String youtubeApiError();

    @MessageTemplate("{youtube_channel_not_found")
    String youtubeChannelNotFound(String channelId);

    @MessageTemplate("{youtube_channel_no_results")
    String youtubeChannelNoResults(String query);

    @MessageTemplate("{no_search_results_found}")
    String noSearchResultsFound();

    @MessageTemplate("{youtube_published_on}")
    String youtubePublishedOn(Date date);

    @MessageTemplate("{ping}")
    String ping();

    @MessageTemplate("{pong}")
    String pong();

    @MessageTemplate("{invite_bot}")
    String inviteBot(String botName);

    @MessageTemplate("{skill_not_found_with_name}")
    String skillNotFoundWithName(String skillName);

    @MessageTemplate("{skill_assigned_to_channel}")
    String skillAssignedToChannel(String skillName, String channelName);

    @MessageTemplate("{skill_guild_has_no_skills}")
    String skillGuildHasNoSkills();

    @MessageTemplate("{skill_with_name_already_exists}")
    String skillWithNameAlreadyExists(String skillName, String guildName);

    @MessageTemplate("{skill_added_succesfully}")
    String skillAddedSuccesfully();

    @MessageTemplate("{skill_deleted_non_existing_skill}")
    String skillDeletedNonExistingSkill();

    @MessageTemplate("{skill_deleted_succesfully}")
    String skillDeletedSuccesfully();

    @MessageTemplate("{skill_delete_all_skills}")
    String skillDeleteAllSkills(int numberDeleted);

    @MessageTemplate("{skill_setting_not_changed}")
    String skillSettingNotChanged();

    @MessageTemplate("{skill_notification_on}")
    String skillNotificationOn();

    @MessageTemplate("{skill_notification_off}")
    String skillNotificationOff();

    @MessageTemplate("{skill_title}")
    String skillTitle();

    @MessageTemplate("{skill_not_assigned}")
    String skillNotAssigned();

    @MessageTemplate("{skill_assigned_to_channelsskill_assigned_to_channels}")
    String skillAssignedToChannels(String channelMentions);

    @MessageTemplate("{receive_notifications}")
    String receiveNotifications();

    @MessageTemplate("{status}")
    String status();

    @MessageTemplate("{enabled}")
    String enabled();

    @MessageTemplate("{disabled}")
    String disabled();

    @MessageTemplate("{prefix_has_been_changed}")
    String prefixHasBeenChanged(String newPrefix);

    @MessageTemplate("{disable_prefix_mentions_only}")
    String disablePrefixMentionsOnly();

    @MessageTemplate("{feature_enabled_try_it_now}")
    String featureEnabledTryItNow();

    @MessageTemplate("{reaction_feature_disabled}")
    String reactionFeatureDisabled();

    @MessageTemplate("{voice_no_one_else_in_channel}")
    String voiceNoOneElseInChannel();

    @MessageTemplate("{voice_no_one_in_channel}")
    String voiceNoOneInChannel();

    @MessageTemplate("{locale_updated_guild}")
    String localeUpdatedGuild(String newLocale);

    @MessageTemplate("{locale_updated_channel}")
    String localeUpdatedChannel(String newLocale);

    @MessageTemplate("{guild_set_new_description}")
    String guildSetNewDescription(String newDescription);

    @MessageTemplate("{guild_change_description}")
    String guildChangeDescription(String newDescription);

    @MessageTemplate("{guild_same_description_as_before}")
    String guildSameDescriptionAsBefore();

    @MessageTemplate("{utilities_total_characters}")
    String utilitiesTotalCharacters(int characters);

    @MessageTemplate("{levels_set_message}")
    String levelsSetMessage();

    @MessageTemplate("{not_storing_guild_data_yet}")
    String notStoringGuildDataYet();

    @MessageTemplate("{level_no_message}")
    String levelNoMessage();

    @MessageTemplate("{assignable_roles_no_data}")
    String assignableRolesNoData();

    @MessageTemplate("{assignable_roles_no_roles_set}")
    String assignableRolesNoRolesSet();

    @MessageTemplate("{assignable_roles_list}")
    String assignableRolesList(String assignableRoleNames);

    @MessageTemplate("{assignable_roles_now_allowed}")
    String assignableRolesNowAllowed(String rolesList);

    @MessageTemplate("{assignable_roles_already_allowed}")
    String assignableRolesAlreadyAllowed(String rolesList);

    @MessageTemplate("{assignable_roles_ignored_duplicates}")
    String assignableRolesIgnoredDuplicates(String rolesList);

    @MessageTemplate("{assignable_roles_removed}")
    String assignableRolesRemoved(String rolesList);

    @MessageTemplate("{assignable_roles_didnt_exist}")
    String assignableRolesDidntExist(String rolesList);

    @MessageTemplate("{assignable_roles_user_assigned_role}")
    String assignableRolesUserAssignedRole(String rolesList);

    @MessageTemplate("{assignable_roles_user_denied_role}")
    String assignableRolesUserDeniedRole(String rolesList);

    @MessageTemplate("{assignable_roles_user_already_had_role}")
    String assignableRolesUserAlreadyHadRole(String rolesList);

    @MessageTemplate("{runescape_total_xp}")
    String runescapeTotalXp();

    @MessageTemplate("{runescape_combat_level}")
    String runescapeCombatLevel();

    @MessageTemplate("{runescape_total_level}")
    String runescapeTotalLevel();

    @MessageTemplate("{runescape_rank}")
    String runescapeRank();

    @MessageTemplate("{runescape_quest_statuses_title}")
    String runescapeQuestStatusesTitle();

    @MessageTemplate("{runescape_quest_statuses}")
    String runeScapeQuestStatuses(String greenCircle, int completedQuests, String yellowCircle, int startedQuests, String redCircle, int notStartedQuests);

    @MessageTemplate("{runescape_latest_activity}")
    String runescapeLatestActivity(LocalDateTime date);

    @MessageTemplate("{runescape_metrics_set_to_private}")
    String runescapeMetricsSetToPrivate(String username);

    @MessageTemplate("{runescape_metrics_not_active_account}")
    String runescapeMetricsNotActiveAccount(String username);

    @MessageTemplate("{runescape_metrics_user_not_found}")
    String runescapeMetricsUserNotFound(String username);

    @MessageTemplate("{quests_complete}")
    String questsComplete();

    @MessageTemplate("{quests_started}")
    String questsStarted();

    @MessageTemplate("{quests_not_started}")
    String questsNotStarted();

    @MessageTemplate("{emote_tracking_enabled}")
    String emoteTrackingEnabled();

    @MessageTemplate("{emote_tracking_set_to_same}")
    String emoteTrackingSetToSame();

    @MessageTemplate("{emote_tracking_setting_changed}")
    String emoteTrackingSettingChanged(String enabledOrDisabled);

    @MessageTemplate("{all_greeting_messages_set_to_channel}")
    String allGreetingMessagesSetToChannel(String guildName, String channelMention);

    /**
     * @return The message to display on a generic callback thread
     * for any network request.
     */
    @MessageTemplate("{generic_network_error}")
    String genericNetworkError();
}
