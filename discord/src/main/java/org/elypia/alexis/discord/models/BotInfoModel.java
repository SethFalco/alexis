package org.elypia.alexis.discord.models;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
public class BotInfoModel {

    /** The name of the author of the bot. */
    private String authorName;

    /** A link to the authors website. */
    private String authorUrl;

    /** The link to the authors logo. */
    private String authorLogo;

    /**
     * The text to display for the support guild, should
     * be something that indicates the user is already in it,
     * or an invite link to it.
     */
    private String supportGuildText;

    /** The name of the application. */
    private String botName;

    /** A description of the bot, taken from the Discord API. */
    private String botDescription;

    private String botAvatarUrl;
    private String botInviteUrl;
    private long botId;
    private int totalGuilds;
    private int totalUsers;
    private int totalBots;

    public String getAuthorName() {
        return authorName;
    }

    public BotInfoModel setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public BotInfoModel setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getAuthorLogo() {
        return authorLogo;
    }

    public BotInfoModel setAuthorLogo(String authorLogo) {
        this.authorLogo = authorLogo;
        return this;
    }

    public String getSupportGuildText() {
        return supportGuildText;
    }

    public BotInfoModel setSupportGuildText(String supportGuildText) {
        this.supportGuildText = supportGuildText;
        return this;
    }

    public String getBotName() {
        return botName;
    }

    public BotInfoModel setBotName(String botName) {
        this.botName = botName;
        return this;
    }

    public String getBotDescription() {
        return botDescription;
    }

    public BotInfoModel setBotDescription(String botDescription) {
        this.botDescription = botDescription;
        return this;
    }

    public String getBotAvatarUrl() {
        return botAvatarUrl;
    }

    public BotInfoModel setBotAvatarUrl(String botAvatarUrl) {
        this.botAvatarUrl = botAvatarUrl;
        return this;
    }

    public String getBotInviteUrl() {
        return botInviteUrl;
    }

    public BotInfoModel setBotInviteUrl(String botInviteUrl) {
        this.botInviteUrl = botInviteUrl;
        return this;
    }

    public long getBotId() {
        return botId;
    }

    public BotInfoModel setBotId(long botId) {
        this.botId = botId;
        return this;
    }

    public int getTotalGuilds() {
        return totalGuilds;
    }

    public BotInfoModel setTotalGuilds(int totalGuilds) {
        this.totalGuilds = totalGuilds;
        return this;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public BotInfoModel setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }

    public int getTotalBots() {
        return totalBots;
    }

    public BotInfoModel setTotalBots(int totalBots) {
        this.totalBots = totalBots;
        return this;
    }
}
