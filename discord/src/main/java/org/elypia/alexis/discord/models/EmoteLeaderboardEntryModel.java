package org.elypia.alexis.discord.models;

import net.dv8tion.jda.api.entities.Emote;
import org.elypia.alexis.persistence.entities.EmoteUsage;

/**
 * Model object that represents the leaderboard for {@link EmoteUsage}.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
public class EmoteLeaderboardEntryModel implements Comparable<EmoteLeaderboardEntryModel> {

    /** The emote that was used. */
    private Emote emote;

    /** The number of times it was used in the current guild in total. */
    private int localeUsage;

    /**
     * The number of times it was used across all
     * guilds including the current one in total.
     */
    private int globalUsage;

    public EmoteLeaderboardEntryModel() {
        // Do nothing.
    }

    public EmoteLeaderboardEntryModel(Emote emote, int localUsage, int globalUsage) {
        this.emote = emote;
        this.localeUsage = localUsage;
        this.globalUsage = globalUsage;
    }

    public Emote getEmote() {
        return emote;
    }

    public void setEmote(Emote emote) {
        this.emote = emote;
    }

    public int getLocaleUsage() {
        return localeUsage;
    }

    public void setLocaleUsage(int localeUsage) {
        this.localeUsage = localeUsage;
    }

    public int getGlobalUsage() {
        return globalUsage;
    }

    public void setGlobalUsage(int globalUsage) {
        this.globalUsage = globalUsage;
    }

    @Override
    public int compareTo(EmoteLeaderboardEntryModel o) {
        int local = Integer.compare(o.localeUsage, localeUsage);

        if (local != 0)
            return local;

        return Integer.compare(o.globalUsage, globalUsage);
    }
}
